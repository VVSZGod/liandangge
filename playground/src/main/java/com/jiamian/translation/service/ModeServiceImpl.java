package com.jiamian.translation.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.jiamian.translation.common.entity.Page;
import com.jiamian.translation.common.enums.YesOrNo;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.dao.redis.ModelRedisService;
import com.jiamian.translation.dao.repository.MetaRepository;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import com.jiamian.translation.dao.repository.ModelRepository;
import com.jiamian.translation.entity.dto.MetaDTO;
import com.jiamian.translation.entity.response.ModelDetailResponse;
import com.jiamian.translation.entity.response.ModelResponse;
import com.jiamian.translation.model.Meta;
import com.jiamian.translation.model.Model;
import com.jiamian.translation.model.ModelCreator;
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

	@Autowired
	private MetaRepository metaRepository;

	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private ModelRedisService modelRedisService;
	@Autowired
	private ModelCreatorRepository modelCreatorRepository;

	public Page<ModelResponse> pageModel(Integer pageNo, Integer pageSize, String key) {

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize,
				Sort.Direction.DESC, "downloadCount", "modelId");
		Specification<Model> specification = (Specification<Model>) (
				root, criteriaQuery, cb) -> {
			List<Predicate> predicates = Lists.newArrayList();
			List<Predicate> predicatesOr = Lists.newArrayList();
			predicates.add(cb.equal(root.get("status"), YesOrNo.YES.value()));
			if (ObjectUtil.isNotEmpty(key)) {
				predicatesOr.add(cb.like(root.get("name"), "%" + key + "%"));
				try {
					predicatesOr.add(cb.equal(root.get("modelId"), Long.parseLong(key)));
				} catch (Exception e) {
					log.info("message{}===id{}", e.getMessage(), key);
				}
				predicates.add(cb.or(predicatesOr.toArray(new Predicate[]{})));
			}
			return cb.and(predicates.toArray(new Predicate[]{}));
		};
		org.springframework.data.domain.Page<ModelResponse> rs = modelRepository
				.findAll(specification, pageRequest)
				.map(new Function<Model, ModelResponse>() {
					@Override
					public ModelResponse apply(
							Model model) {
						ModelResponse modelResponse = new ModelResponse();
						BeanUtils.copyProperties(model, modelResponse);
						Optional<Meta> meta = metaRepository.selectModelByModelIdOne(model.getModelId());
						if (meta.isPresent() && StringUtils.isNotEmpty(meta.get().getQiniuUrl())) {
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
		Optional<Model> optionalModel = modelRepository.findByModelId(modelId);
		if (optionalModel.isPresent()) {
			Model model = optionalModel.get();
			List<ModelCreator> modelCreators = modelCreatorRepository.findByModelId(model.getModelId());

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
			}

			modelDetailResponse.setMetaDTOList(metaDTOList);
		} else {
			throw new BOException("该模型不存在");
		}
		return modelDetailResponse;
	}

	public Map<String, String> getModelUrl(Integer modelId) {
		Optional<Model> model = modelRepository.findByModelId(modelId.longValue());
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
			int selectModelUploadCount = modelRepository.selectModelUploadCount();
			map.put("modelUploadCount", selectModelUploadCount);
			modelRedisService.setModelUploadCount(selectModelUploadCount);
		}
		return map;
	}
}
