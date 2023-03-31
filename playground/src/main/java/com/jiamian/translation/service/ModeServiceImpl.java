package com.jiamian.translation.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import com.jiamian.translation.entity.response.ModelTypeResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.jiamian.translation.common.entity.Page;
import com.jiamian.translation.common.enums.SortTypeEnum;
import com.jiamian.translation.common.enums.YesOrNo;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.dao.redis.ModelRedisService;
import com.jiamian.translation.dao.repository.MetaRepository;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import com.jiamian.translation.dao.repository.ModelRepository;
import com.jiamian.translation.dao.repository.ModelTagsRepository;
import com.jiamian.translation.entity.dto.api.MetaDTO;
import com.jiamian.translation.entity.response.ModelDetailResponse;
import com.jiamian.translation.entity.response.ModelResponse;
import com.jiamian.translation.model.Meta;
import com.jiamian.translation.model.Model;
import com.jiamian.translation.model.ModelCreator;
import com.jiamian.translation.model.ModelTags;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	public Page<ModelResponse> pageModel(Integer pageNo, Integer pageSize,
			String key, String type, Integer sortType) {
		String[] desc = new String[] { "downloadCount", "modelId" };
		if (SortTypeEnum.DOWN_COUNT.value().equals(sortType)) {

		} else if (SortTypeEnum.TIME.value().equals(sortType)) {
			desc = new String[] { "createDate", "downloadCount" };
		} else {
			desc = new String[] { "rating", "downloadCount" };
		}
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize,
				Sort.Direction.DESC, desc);
		Specification<Model> specification = (Specification<Model>) (root,
				criteriaQuery, cb) -> {
			List<Predicate> predicates = Lists.newArrayList();
			List<Predicate> predicatesOr = Lists.newArrayList();
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
			return cb.and(predicates.toArray(new Predicate[] {}));
		};
		org.springframework.data.domain.Page<ModelResponse> rs = modelRepository
				.findAll(specification, pageRequest)
				.map(new Function<Model, ModelResponse>() {
					@Override
					public ModelResponse apply(Model model) {
						ModelResponse modelResponse = new ModelResponse();
						BeanUtils.copyProperties(model, modelResponse);
						Optional<Meta> meta = metaRepository
								.selectModelByModelIdOne(model.getModelId());
						if (meta.isPresent() && StringUtils
								.isNotEmpty(meta.get().getQiniuUrl())) {
							modelResponse.setImageUrl(meta.get().getQiniuUrl());
						}
						if (model.getAliUrl().isEmpty()) {
							modelResponse.setDownloadCount(0);
							modelResponse.setRating("0.0");
						}
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

	public ModelDetailResponse modelDetail(Long userId, Long modelId) {
		ModelDetailResponse modelDetailResponse = new ModelDetailResponse();
		Optional<Model> optionalModel = modelRepository
				.findByModelIdAndStatus(modelId, YesOrNo.YES.value());
		if (optionalModel.isPresent()) {
			Model model = optionalModel.get();
			List<ModelCreator> modelCreators = modelCreatorRepository
					.findByModelId(model.getModelId());

			BeanUtil.copyProperties(model, modelDetailResponse);
			List<Meta> metaList = metaRepository.findByModelId(modelId);
			List<MetaDTO> metaDTOList = metaList.stream().map(meta -> {
				MetaDTO metaDTO = new MetaDTO();
				BeanUtil.copyProperties(meta, metaDTO);
				return metaDTO;
			}).collect(Collectors.toList());
			modelDetailResponse.setModelUrl("");
			if (model.getAliUrl().isEmpty()) {
				modelDetailResponse.setDownloadCount(0);
				modelDetailResponse.setRating("0.0");
			}

			if (CollectionUtil.isNotEmpty(modelCreators)) {
				String username = modelCreators.get(0).getUsername();
				String headThumb = modelCreators.get(0).getImage();
				modelDetailResponse.setCreatorUserName(username);
				modelDetailResponse.setCreatorHeadThumb(headThumb);
				modelDetailResponse.setCreatorLink(
						String.format(C_CREATOR_LINK, username));
			}
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

				modelDetailResponse.setTrainedWords(
						Arrays.asList(trainedWords.trim().split(",")));
				modelDetailResponse
						.setTags(Arrays.asList(tagText.trim().split(",")));
				modelDetailResponse.setBaseModel(baseModel);

			}

			modelDetailResponse.setMetaDTOList(metaDTOList);
		} else {
			throw new BOException("该模型不存在");
		}
		return modelDetailResponse;
	}

	public Map<String, String> getModelUrl(Integer modelId) {
		Optional<Model> model = modelRepository.findByModelIdAndStatus(
				modelId.longValue(), YesOrNo.YES.value());
		if (model.isPresent()) {
			Map<String, String> map = new HashMap<>();
			String aliUrl = model.get().getAliUrl();
			if (StringUtils.isNotEmpty(aliUrl)) {
				map.put("aliUrl", aliUrl);
				map.put("aliPwd", model.get().getAliPwd());
				return map;
			} else {
				throw new BOException("模型还在上传中 请稍后再试");
			}
		} else {
			throw new BOException("该模型不存在");
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
}
