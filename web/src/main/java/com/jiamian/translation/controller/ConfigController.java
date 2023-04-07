package com.jiamian.translation.controller;

import cn.hutool.core.util.ObjectUtil;
import com.jiamian.translation.exception.BOException;
import com.jiamian.translation.exception.ErrorMsg;
import com.jiamian.translation.request.ModelClaimReq;
import com.jiamian.translation.service.ModelClaimServiceImpl;
import com.jiamian.translation.service.WorkOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiamian.translation.annotation.LoginUser;
import com.jiamian.translation.entity.JsonResult;
import com.jiamian.translation.request.WorkOrderReq;
import com.jiamian.translation.util.UserTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author DingGuangHui
 * @date 2023/2/27
 */
@Api(tags = "报告问题、举报、模型认领")
@RequestMapping("/config")
@RestController
public class ConfigController {

	@Autowired
	private WorkOrderServiceImpl workOrderService;

	@Autowired
	private ModelClaimServiceImpl modelClaimService;

	@PostMapping("/work/order/save")
	@ApiOperation("提交报告问题、举报")
	public JsonResult createWorkOrder(@RequestBody WorkOrderReq workOrderReq,
			@LoginUser Long userId) {
		UserTokenUtil.needLogin(userId);
		if (workOrderReq.getTitle().isEmpty()
				|| workOrderReq.getTitle().length() > 49) {
			throw new BOException("标题" + ErrorMsg.PARAMETER_LONG_ERROR);
		}
		if (workOrderReq.getType().isEmpty()
				|| workOrderReq.getType().length() > 19) {
			throw new BOException("类型" + ErrorMsg.PARAMETER_LONG_ERROR);
		}
		workOrderService.createWorkOrder(workOrderReq, userId);
		return JsonResult.succResult();
	}

	@PostMapping("/model/claim/save")
	@ApiOperation("提交模型认领")
	public JsonResult createModelClaim(@RequestBody ModelClaimReq modelClaimReq,
			@LoginUser Long userId) {
		UserTokenUtil.needLogin(userId);
		if (modelClaimReq.getTitle().isEmpty()
				|| modelClaimReq.getTitle().length() > 49) {
			throw new BOException("标题" + ErrorMsg.PARAMETER_LONG_ERROR);
		}
		if (ObjectUtil.isNull(modelClaimReq.getModelId())) {
			throw new BOException("模型id不能为空");
		}
		modelClaimService.createModelClaim(modelClaimReq, userId);
		return JsonResult.succResult();
	}

}
