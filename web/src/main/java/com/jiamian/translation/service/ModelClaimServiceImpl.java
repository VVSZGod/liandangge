package com.jiamian.translation.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiamian.translation.dao.model.ModelClaim;
import com.jiamian.translation.dao.repository.ModelClaimRepository;
import com.jiamian.translation.request.ModelClaimReq;

import cn.hutool.core.bean.BeanUtil;

@Service
public class ModelClaimServiceImpl {

	@Autowired
	private ModelClaimRepository modelClaimRepository;

	@Transactional(rollbackFor = Exception.class)
	public void createModelClaim(ModelClaimReq modelClaimReq, Long userId) {
		ModelClaim modelClaim = new ModelClaim();
		BeanUtil.copyProperties(modelClaimReq, modelClaim);
		modelClaim.setUserId(userId);
		modelClaim.setCreateTime(LocalDateTime.now());
		modelClaimRepository.save(modelClaim);
	}
}
