package com.jiamian.translation.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import com.jiamian.translation.util.DateUtil;
import com.jiamian.translation.util.PictureUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jiamian.translation.common.entity.Page;
import com.jiamian.translation.common.enums.SortTypeEnum;
import com.jiamian.translation.common.enums.YesOrNo;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.common.exception.ErrorMsg;
import com.jiamian.translation.dao.ModelServiceDao;
import com.jiamian.translation.dao.redis.ModelRedisService;
import com.jiamian.translation.dao.repository.MetaRepository;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import com.jiamian.translation.dao.repository.ModelRepository;
import com.jiamian.translation.dao.repository.ModelTagsRepository;
import com.jiamian.translation.entity.dto.api.MetaDTO;
import com.jiamian.translation.entity.response.ModelDetailResponse;
import com.jiamian.translation.entity.response.ModelResponse;
import com.jiamian.translation.entity.response.ModelTypeResponse;
import com.jiamian.translation.model.Meta;
import com.jiamian.translation.model.Model;
import com.jiamian.translation.model.ModelCreator;
import com.jiamian.translation.model.ModelTags;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ModeServiceImpl
 * @Auther: z1115
 * @Date: 2023/3/26 10:09
 * @Description: TODOplate_label
 * @Version: 1.0
 */
@Service
@Slf4j
public class ModeServiceImpl {

	private static final String C_CREATOR_LINK = "https://civitai.com/user/%s";

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private MetaRepository metaRepository;

	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ModelRedisService modelRedisService;
	@Autowired
	private ModelCreatorRepository modelCreatorRepository;

	@Autowired
	private ModelTagsRepository modelTagsRepository;

	@Autowired
	private ModelTypeServiceImpl modelTypeService;

	@Autowired
	private ModelServiceDao modelServiceDao;

	@Transactional(rollbackFor = Exception.class)
	public Page<ModelResponse> pageModel(Integer pageNo, Integer pageSize,
			String key, String type, Integer sortType, Long userId) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Specification<Model> specification = (Specification<Model>) (root,
				criteriaQuery, cb) -> {
			List<Predicate> predicates = Lists.newArrayList();
			List<Predicate> predicatesOr = Lists.newArrayList();
			List<Order> list = new ArrayList<>();
			predicates.add(cb.equal(root.get("status"), YesOrNo.YES.value()));
			if (ObjectUtil.isNotEmpty(key)) {
				predicatesOr.add(cb.like(root.get("name"), "%" + key + "%"));
				try {
					predicatesOr.add(
							cb.equal(root.get("modelId"), Long.parseLong(key)));
				} catch (Exception e) {
					log.info("message{}===id{}", e.getMessage(), key);
				}
				predicates.add(cb.or(predicatesOr.toArray(new Predicate[] {})));
			}
			if (StringUtils.isNotEmpty(type)) {
				if (type.equals("Other")) {
					List<ModelTypeResponse> modelTypeResponses = modelTypeService
							.notShowModelTypeResponseList();
					CriteriaBuilder.In<Object> types = cb.in(root.get("type"));
					types.value("Other");
					for (ModelTypeResponse modelTypeResponse : modelTypeResponses) {
						types.value(modelTypeResponse.getType());
					}
					predicates.add(types);
				} else {
					predicates.add(cb.equal(root.get("type"), type));
				}
			}
			if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
				list.add(cb.desc(cb.sum(root.get("downloadCount"),
						root.get("ldgDownloadCount"))));
			} else if (SortTypeEnum.TIME.value().equals(sortType)) {
				list.add((cb.desc(root.get("createDate"))));
			} else {
				list.add(cb.desc(root.get("rating")));
			}
			list.add(cb.desc(root.get("modelId")));
			criteriaQuery.orderBy(list);
			return cb.and(predicates.toArray(new Predicate[] {}));
		};
		org.springframework.data.domain.Page<ModelResponse> rs = modelRepository
				.findAll(specification, pageRequest)
				.map(new Function<Model, ModelResponse>() {
					@Override
					public ModelResponse apply(Model model) {
						ModelResponse modelResponse = new ModelResponse();
						BeanUtils.copyProperties(model, modelResponse);
						setModelData(modelResponse, model.getAliUrl(), userId,
								model.getLdgDownloadCount());
						modelResponse
								.setCreateDate(model.getCreateDate() == null
										? LocalDateTime.of(2020, 2, 2, 2, 2)
										: model.getCreateDate());
						return modelResponse;
					}
				});
		com.jiamian.translation.common.entity.Page<ModelResponse> p = new com.jiamian.translation.common.entity.Page<>();
		p.setList(Lists.newArrayList(rs.getContent()));
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setTotalPages(rs.getTotalPages());
		p.setTotalRecords((int) rs.getTotalElements());
		return p;
	}

	@Transactional(rollbackFor = Exception.class)
	public ModelDetailResponse modelDetail(Long userId, Long modelId) {
		ModelDetailResponse modelDetailResponse = new ModelDetailResponse();
		ModelResponse modelResponse = new ModelResponse();
		Optional<Model> optionalModel = modelRepository
				.findByModelIdAndStatus(modelId, YesOrNo.YES.value());
		if (optionalModel.isPresent()) {
			Model model = optionalModel.get();
			BeanUtil.copyProperties(model, modelResponse);
			this.setModelData(modelResponse, model.getAliUrl(), userId,
					model.getLdgDownloadCount());
			BeanUtil.copyProperties(modelResponse, modelDetailResponse);
			modelDetailResponse.setVersion(model.getVersion());
			List<Meta> metaList = metaRepository.findByModelId(modelId);
			List<MetaDTO> metaDTOList = metaList.stream().map(meta -> {
				MetaDTO metaDTO = new MetaDTO();
				BeanUtil.copyProperties(meta, metaDTO);
				metaDTO.setQiniuUrl(meta.getQiniuUrl() + "-mshalf");
				return metaDTO;
			}).collect(Collectors.toList());
			List<ModelTags> modelTags = modelTagsRepository
					.findByModelId(modelId);
			if (CollectionUtil.isNotEmpty(modelTags)) {
				ModelTags modelTag = modelTags.get(0);
				String baseModel = modelTag.getBaseModel();
				String trainedWords = ObjectUtils.isNotEmpty(
						modelTag.getTrainedWords()) ? modelTag.getTrainedWords()
								: "";
				String tagText = ObjectUtils.isNotEmpty(modelTag.getTagText())
						? modelTag.getTagText()
						: "";
				modelDetailResponse.setTrainedWords(new ArrayList<>());
				if (StringUtils.isNotEmpty(trainedWords)) {
					modelDetailResponse.setTrainedWords(
							Arrays.asList(trainedWords.trim().split(",")));
				}
				modelDetailResponse
						.setTags(Arrays.asList(tagText.trim().split(",")));
				modelDetailResponse.setBaseModel(baseModel);

			}

			modelDetailResponse.setMetaDTOList(metaDTOList);
		} else {
			throw new BOException(ErrorMsg.MODEL_NOT_FOUND_ERROR);
		}
		return modelDetailResponse;
	}

	/**
	 * 获取模型下载 链接成功 记一次ldg下载数
	 *
	 * @param modelId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> getModelUrl(Integer modelId) {
		Optional<Model> optionalModel = modelRepository.findByModelIdAndStatus(
				modelId.longValue(), YesOrNo.YES.value());
		if (optionalModel.isPresent()) {
			Map<String, String> map = new HashMap<>();
			Model model = optionalModel.get();
			String aliUrl = model.getAliUrl();
			if (StringUtils.isNotEmpty(aliUrl)) {
				map.put("aliUrl", aliUrl);
				map.put("aliPwd", model.getAliPwd());
				model.setLdgDownloadCount(model.getLdgDownloadCount() + 1);
				modelRepository.save(model);
				return map;
			} else {
				throw new BOException("模型还在上传中 请稍后再试");
			}
		} else {
			throw new BOException(ErrorMsg.MODEL_NOT_FOUND_ERROR);
		}
	}

	public Map<String, Integer> getModelCount() {
		Map<String, Integer> map = new HashMap<>();
		int modelTotalCount = modelRedisService.getModelTotalCount();
		int modelUploadCount = modelRedisService.getModelUploadCount();
		map.put("modelTotalCount", modelTotalCount);
		map.put("modelUploadCount", modelUploadCount);
		if (modelTotalCount == 0) {
			int selectModelCount = modelRepository.selectModelCount();
			map.put("modelTotalCount", selectModelCount);
			modelRedisService.setModelTotalCount(selectModelCount);
		}
		if (modelUploadCount == 0) {
			int selectModelUploadCount = modelRepository
					.selectModelUploadCount();
			map.put("modelUploadCount", selectModelUploadCount);
			modelRedisService.setModelUploadCount(selectModelUploadCount);
		}
		return map;
	}

	@Transactional(rollbackFor = Exception.class)
	public Page<ModelResponse> modelByTagList(Integer pageNo, Integer pageSize,
			String key, Integer sortType, Long userId) {
		com.jiamian.translation.common.entity.Page<ModelResponse> p = new com.jiamian.translation.common.entity.Page<>();
		List<ModelResponse> listModel = new ArrayList<>();
		// 获取 查询到的 模型结果
		JSONObject modelListByTag = modelServiceDao.getModelListByTag(pageNo,
				pageSize, sortType, key);
		int count = Integer.parseInt(modelListByTag.get("count").toString());
		List<Object[]> list = (List<Object[]>) modelListByTag.get("list");
		if (CollectionUtil.isNotEmpty(list)) {
			for (Object[] value : list) {
				ModelResponse modelResponse = new ModelResponse();
				Long id = Long.parseLong(value[0].toString());
				Long modelId = Long.parseLong(value[1].toString());
				String modelName = value[2].toString();
				String type = value[3].toString();
				LocalDateTime createDate = LocalDateTime.of(2020, 2, 2, 2, 2);
				try {
					if (ObjectUtil.isNotNull(value[4])) {
						createDate = LocalDateTime.parse(
								value[4].toString().substring(0, 19),
								dateTimeFormatter);
					}
				} catch (Exception e) {
					log.info(e.getMessage());
				}
				String description = value[5].toString();
				int downloadCount = Integer.parseInt(value[6].toString());
				String rating = value[7].toString();
				int ldgDownloadCount = Integer.parseInt(value[8].toString());
				String alUrl = value[9].toString();
				modelResponse.setId(id);
				modelResponse.setModelId(modelId);
				modelResponse.setName(modelName);
				modelResponse.setCreateDate(createDate);
				modelResponse.setDescription(description);
				modelResponse.setType(type);
				modelResponse.setDownloadCount(downloadCount);
				modelResponse.setRating(rating);
				this.setModelData(modelResponse, alUrl, userId,
						ldgDownloadCount);
				listModel.add(modelResponse);
			}
		}
		p.setTotalRecords(count);
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setList(listModel);
		p.setTotalPages((count - 1) / pageSize + 1);
		return p;
	}

	/**
	 * 处理 返回数据
	 */

	public void setModelData(ModelResponse modelResponse, String aliUrl,
			Long userId, Integer ldgDownloadCount) {
		Long modelId = modelResponse.getModelId();
		Optional<Meta> optionalMeta = metaRepository
				.selectModelByModelIdOne(modelId);
		if (optionalMeta.isPresent()
				&& StringUtils.isNotEmpty(optionalMeta.get().getQiniuUrl())) {
			Meta meta = optionalMeta.get();
			String qiniuUrl = meta.getQiniuUrl();
			modelResponse.setImageUrl(qiniuUrl + "-mshalf");
			// 判断图片大小，没有则保存图片大小
			if (meta.getHeight() > 0) {
				modelResponse.setImageHeight(meta.getHeight());
				modelResponse.setImageWidth(meta.getWidth());
			} else {
				JSONObject imageInfo = PictureUtil.getImageInfo(qiniuUrl);
				if (imageInfo != null) {
					log.info("保存图片size Url==={}", qiniuUrl);
					Integer width = imageInfo.getInteger("width");
					Integer height = imageInfo.getInteger("height");
					modelResponse.setImageHeight(height);
					modelResponse.setImageWidth(width);
					meta.setHeight(height);
					meta.setWidth(width);
					metaRepository.save(meta);
				}
			}
		}
		modelResponse.setDownloadCount(
				modelResponse.getDownloadCount() + ldgDownloadCount);
		if (aliUrl.isEmpty()) {
			modelResponse.setDownloadCount(0);
		}
		List<ModelCreator> modelCreators = modelCreatorRepository
				.findByModelId(modelId);
		if (CollectionUtil.isNotEmpty(modelCreators)) {
			String username = modelCreators.get(0).getUsername();
			String headThumb = modelCreators.get(0).getImage();
			modelResponse.setCreatorUserName(username);
			modelResponse.setCreatorHeadThumb(headThumb);
			modelResponse
					.setCreatorLink(String.format(C_CREATOR_LINK, username));
		}
		if (userId == 0L) {
			modelResponse.setCollectStat(false);
		} else {
			modelResponse.setCollectStat(
					modelRedisService.isCollectionModelUser(userId, modelId));
		}

	}

	public void userCollectionModel(Long userId, Long modelId) {
		Optional<Model> optionalModel = modelRepository
				.findByModelIdAndStatus(modelId, YesOrNo.YES.value());
		if (!optionalModel.isPresent()) {
			throw new BOException(ErrorMsg.MODEL_NOT_FOUND_ERROR);
		}
		Boolean follow = modelRedisService.isCollectionModelUser(userId,
				modelId);
		if (follow) {
			modelRedisService.removeCollectionModelUser(userId, modelId);
		} else {
			// 添加到自己的收藏列表
			modelRedisService.addCollectionModelUser(userId, modelId);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public Page<ModelResponse> userCollectionModelList(Integer pageNo,
			Integer pageSize, Long userId) {
		com.jiamian.translation.common.entity.Page<ModelResponse> p = new com.jiamian.translation.common.entity.Page<>();
		List<ModelResponse> modelResponses = new ArrayList<>();
		Set<ZSetOperations.TypedTuple<String>> typedTuples = modelRedisService
				.userCollectionModelList(userId, pageNo, pageSize);
		for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
			Long modelId = Long
					.parseLong(String.valueOf(typedTuple.getValue()));
			Double score = typedTuple.getScore();
			ModelResponse modelResponse = new ModelResponse();
			Optional<Model> optionalModel = modelRepository
					.findByModelId(modelId);
			BeanUtil.copyProperties(optionalModel.get(), modelResponse);
			this.setModelData(modelResponse, optionalModel.get().getAliUrl(),
					userId, optionalModel.get().getLdgDownloadCount());
			modelResponse.setCollectDate(
					DateUtil.getLocalDateTimeFromTimeStamp(score.longValue()));
			modelResponse.setCollectStat(true);
			modelResponses.add(modelResponse);
		}
		int modelCount = modelRedisService.userCollectionModelCount(userId);
		p.setList(modelResponses);
		p.setTotalRecords(modelCount);
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setTotalPages((modelCount - 1) / pageSize + 1);
		return p;
	}
}
