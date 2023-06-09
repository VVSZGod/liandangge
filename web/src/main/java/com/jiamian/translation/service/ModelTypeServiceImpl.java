package com.jiamian.translation.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.jiamian.translation.enums.YesOrNo;
import com.jiamian.translation.redis.ModelRedisService;
import com.jiamian.translation.dao.repository.ModelTypeRepository;
import com.jiamian.translation.entity.response.ModelTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelTypeServiceImpl {

	@Autowired
	private ModelTypeRepository modelTypeRepository;

	@Autowired
	private ModelRedisService modelRedisService;

	public List<ModelTypeResponse> modelTypeResponseList() {
		List<ModelTypeResponse> modelTypeResponses;
		String modelTypeValue = modelRedisService.getModelType();
		if (modelTypeValue.isEmpty()) {
			modelTypeResponses = modelTypeRepository
					.selectModeTypeList(YesOrNo.YES.value()).stream()
					.map(modelType -> {
						ModelTypeResponse modelTypeResponse = new ModelTypeResponse();
						BeanUtil.copyProperties(modelType, modelTypeResponse);
						return modelTypeResponse;
					}).collect(Collectors.toList());
			modelRedisService
					.setModelType(JSON.toJSONString(modelTypeResponses));
		} else {
			modelTypeResponses = JSON.parseArray(modelTypeValue,
					ModelTypeResponse.class);
		}
		return modelTypeResponses;
	}

	public List<ModelTypeResponse> notShowModelTypeResponseList() {
		List<ModelTypeResponse> modelTypeResponses;
		String modelTypeValue = modelRedisService.getNotShowModelType();
		if (modelTypeValue.isEmpty()) {
			modelTypeResponses = modelTypeRepository
					.selectModeTypeList(YesOrNo.NO.value()).stream()
					.map(modelType -> {
						ModelTypeResponse modelTypeResponse = new ModelTypeResponse();
						BeanUtil.copyProperties(modelType, modelTypeResponse);
						return modelTypeResponse;
					}).collect(Collectors.toList());
			modelRedisService
					.setNotShowModelType(JSON.toJSONString(modelTypeResponses));
		} else {
			modelTypeResponses = JSON.parseArray(modelTypeValue,
					ModelTypeResponse.class);
		}
		return modelTypeResponses;
	}
}
