package com.jiamian.translation.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import com.jiamian.translation.entity.Page;
import com.jiamian.translation.enums.SortTypeEnum;
import com.jiamian.translation.enums.YesOrNo;
import com.jiamian.translation.exception.BOException;
import com.jiamian.translation.exception.ErrorMsg;
import com.jiamian.translation.dao.ModelServiceDao;
import com.jiamian.translation.redis.ModelRedisService;
import com.jiamian.translation.dao.repository.MetaRepository;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import com.jiamian.translation.dao.repository.ModelRepository;
import com.jiamian.translation.dao.repository.ModelTagsRepository;
import com.jiamian.translation.entity.dto.api.MetaDTO;
import com.jiamian.translation.entity.response.ModelDetailResponse;
import com.jiamian.translation.entity.response.ModelResponse;
import com.jiamian.translation.entity.response.ModelTypeResponse;
import com.jiamian.translation.dao.model.Meta;
import com.jiamian.translation.dao.model.Model;
import com.jiamian.translation.dao.model.ModelCreator;
import com.jiamian.translation.dao.model.ModelTags;

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

	@Autowired
	private ModelCreatorServiceImpl modelCreatorService;

	@PersistenceContext
	EntityManager entityManager;

	@Transactional(rollbackFor = Exception.class)
	public Page<ModelResponse> pageModel(Integer pageNo, Integer pageSize,
			String key, String type, Integer sortType, Long userId,
			Integer chine, Integer recommend) {
		com.jiamian.translation.entity.Page<ModelResponse> p = new com.jiamian.translation.entity.Page<>();
		List<ModelResponse> listModel = new ArrayList<>();
		JSONObject jsonObject = this.searchModelList(pageNo, pageSize, key,
				type, sortType, userId, chine, recommend);
		int count = Integer.parseInt(jsonObject.get("count").toString());
		List<Object[]> list = (List<Object[]>) jsonObject.get("list");
		if (CollectionUtil.isNotEmpty(list)) {
			this.wrapModelList(list, listModel, userId);
		}
		p.setTotalRecords(count);
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setList(listModel);
		p.setTotalPages((count - 1) / pageSize + 1);
		return p;
	}

	@Transactional(rollbackFor = Exception.class)
	public ModelDetailResponse modelDetail(Long userId, Long modelId,
			Long modelVersionId) {
		ModelDetailResponse modelDetailResponse = new ModelDetailResponse();
		ModelResponse modelResponse = new ModelResponse();
		Optional<Model> optionalModel = modelRepository
				.findByModelIdAndStatusAndModelVersionId(modelId,
						YesOrNo.YES.value(), modelVersionId);
		if (optionalModel.isPresent()) {
			Model model = optionalModel.get();
			BeanUtil.copyProperties(model, modelResponse);
			this.setModelData(modelResponse, model.getAliUrl(), userId,
					model.getLdgDownloadCount());
			BeanUtil.copyProperties(modelResponse, modelDetailResponse);
			modelDetailResponse.setVersion(model.getVersion());
			List<Meta> metaList = metaRepository
					.findByModelIdAndModelVersionId(modelId, modelVersionId);
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
	public Map<String, String> getModelUrl(Integer modelId,
			Long modelVersionId) {
		Optional<Model> optionalModel = modelRepository
				.findByModelIdAndStatusAndModelVersionId(modelId.longValue(),
						YesOrNo.YES.value(), modelVersionId);
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
		com.jiamian.translation.entity.Page<ModelResponse> p = new com.jiamian.translation.entity.Page<>();
		List<ModelResponse> listModel = new ArrayList<>();
		// 获取 查询到的 模型结果
		JSONObject modelListByTag = modelServiceDao.getModelListByTag(pageNo,
				pageSize, sortType, key);
		int count = Integer.parseInt(modelListByTag.get("count").toString());
		List<Object[]> list = (List<Object[]>) modelListByTag.get("list");
		if (CollectionUtil.isNotEmpty(list)) {
			this.wrapModelList(list, listModel, userId);
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
		Optional<Meta> optionalMeta = metaRepository.selectModelByModelIdOne(
				modelId, modelResponse.getModelVersionId());
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
			Long uid = modelCreators.get(0).getUid();
			modelResponse.setCreatorUserName(username);
			modelResponse.setCreatorHeadThumb(headThumb);
			if (headThumb.startsWith("https://msdn.miaoshouai.com")) {
				modelResponse.setCreatorHeadThumb(headThumb + "-mshalf");
			}
			modelResponse.setModelClaimStat(uid != null);
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
				.selectModelByModelIdAndStatus(modelId, YesOrNo.YES.value());
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
		com.jiamian.translation.entity.Page<ModelResponse> p = new com.jiamian.translation.entity.Page<>();
		List<ModelResponse> modelResponses = new ArrayList<>();
		Set<ZSetOperations.TypedTuple<String>> typedTuples = modelRedisService
				.userCollectionModelList(userId, pageNo, pageSize);
		for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
			Long modelId = Long
					.parseLong(String.valueOf(typedTuple.getValue()));
			Double score = typedTuple.getScore();
			ModelResponse modelResponse = new ModelResponse();
			Optional<Model> optionalModel = modelRepository
					.selectModelByModelId(modelId);
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

	public List<ModelResponse> modelListVersion(Long modelId) {
		List<Model> modelList = modelRepository.findByModelIdAndStatus(modelId,
				YesOrNo.YES.value());
		return modelList.stream().map(model -> {
			ModelResponse modelResponse = new ModelResponse();
			BeanUtil.copyProperties(model, modelResponse);
			return modelResponse;
		}).collect(Collectors.toList());
	}

	public JSONObject searchModelList(Integer pageNo, Integer pageSize,
			String key, String type, Integer sortType, Long userId,
			Integer chine, Integer recommend) {
		JSONObject jsonObject = new JSONObject();
		int firstResult;
		pageNo = pageSize * pageNo;
		StringBuilder sql = new StringBuilder();
		sql.append(" select model_id,");
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
			sql.append(" (downloadCount+ldg_download_count)");
		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			sql.append(" create_date");
		} else {
			sql.append(" rating");
		}
		sql.append(" from  model m where status=1 ");
		StringBuilder sqlCount = new StringBuilder();
		sqlCount.append(
				" select count(*) from (select count(*) from model where status=1");
		if (StringUtils.isNotEmpty(key)) {
			sql.append(" and (name like '%").append(key).append("%' ");
			sqlCount.append(" and (name like '%").append(key).append("%' ");
			if (isNumeric(key)) {
				sql.append(" or model_id=").append(Long.parseLong(key));
				sqlCount.append(" or model_id=").append(Long.parseLong(key));
			}
			List<Long> longs = modelCreatorService.searchModelByUserName(key);
			if (CollectionUtil.isNotEmpty(longs)) {
				sql.append(" or model_id in(");
				sqlCount.append(" or model_id in(");
				for (Long aLong : longs) {
					sql.append(aLong).append(",");
					sqlCount.append(aLong).append(",");
				}
				sql.delete(sql.length() - 1, sql.length());
				sqlCount.delete(sql.length() - 1, sql.length());
				sql.append(")");
				sqlCount.append(")");
			}
			sql.append(" )");
			sqlCount.append(" )");
		}
		if (StringUtils.isNotEmpty(type)) {
			if (type.equals("Other")) {
				sql.append(" and type in('").append("Other',");
				sqlCount.append(" and type in('").append("Other',");
				List<ModelTypeResponse> modelTypeResponses = modelTypeService
						.notShowModelTypeResponseList();
				if (CollectionUtil.isNotEmpty(modelTypeResponses)) {
					for (ModelTypeResponse modelTypeResponse : modelTypeResponses) {
						sql.append("'").append(modelTypeResponse.getType())
								.append("',");
						sqlCount.append("'").append(modelTypeResponse.getType())
								.append("',");
					}
					sql.delete(sql.length() - 1, sql.length());
					sqlCount.delete(sqlCount.length() - 1, sqlCount.length());
					sql.append(")");
					sqlCount.append(")");
				}
			} else {
				sql.append(" and type='").append(type).append("'");
				sqlCount.append(" and type='").append(type).append("'");
			}
		}
		if (ObjectUtil.isNotNull(chine)) {
			sql.append(" and chinese=").append(chine);
			sqlCount.append(" and chinese=").append(chine);
		}
		if (ObjectUtil.isNotNull(recommend)) {
			sql.append(" and recommend=").append(recommend);
			sqlCount.append(" and recommend=").append(recommend);
		}
		sqlCount.append(" group by model_id").append(") a");
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {
			sql.append(" group by model_id,(downloadCount+ldg_download_count)");
			sql.append(
					" order by (downloadCount+ldg_download_count) desc ,model_id desc");
		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			sql.append(" group by model_id,create_date");
			sql.append(" order by create_date desc ,model_id desc");
		} else {
			sql.append(" group by model_id,rating");
			sql.append(" order by rating desc ,model_id desc ");
		}
		sql.append(" limit ").append(pageNo).append(",").append(pageSize);
		Query query = entityManager.createNativeQuery(sql.toString());
		List<Object[]> resultList = query.getResultList();
		Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
		firstResult = Integer.parseInt(queryCount.getSingleResult().toString());
		jsonObject.put("count", firstResult);
		jsonObject.put("list", resultList);
		return jsonObject;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	private void wrapModelList(List<Object[]> list,
			List<ModelResponse> listModel, Long userId) {
		for (Object[] value : list) {
			ModelResponse modelResponse = new ModelResponse();
			Long modelId = Long.parseLong(value[0].toString());
			Model model = modelRepository.selectModelByModelId(modelId).get();
			BeanUtil.copyProperties(model, modelResponse);
			modelResponse.setCreateDate(model.getCreateDate() == null
					? LocalDateTime.of(2020, 2, 2, 2, 2)
					: model.getCreateDate());
			this.setModelData(modelResponse, model.getAliUrl(), userId,
					model.getLdgDownloadCount());
			listModel.add(modelResponse);
		}
	}
}
