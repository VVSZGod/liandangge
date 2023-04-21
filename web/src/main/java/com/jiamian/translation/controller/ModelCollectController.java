package com.jiamian.translation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiamian.translation.annotation.LoginUser;
import com.jiamian.translation.entity.JsonResult;
import com.jiamian.translation.entity.Page;
import com.jiamian.translation.entity.response.ModelResponse;
import com.jiamian.translation.exception.BOException;
import com.jiamian.translation.service.ModeServiceImpl;
import com.jiamian.translation.util.UserTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @ClassName: ModelController
 * @Auther: z1115
 * @Date: 2023/3/26 11:25
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/collect")
@Api(tags = "模型收藏")
public class ModelCollectController {

	@Autowired
	private ModeServiceImpl modeService;

	@PostMapping("/model")
	@ApiOperation("用户收藏模型")
	public JsonResult userCollectionModel(@LoginUser Long userId,
			@RequestParam(value = "modelId") Long modelId) {
		UserTokenUtil.needLogin(userId);
		modeService.userCollectionModel(userId, modelId);
		return JsonResult.succResult();
	}

	@PostMapping("/cancel/model")
	@ApiOperation("用户取消收藏模型")
	public JsonResult cancelUserCollectionModel(@LoginUser Long userId,
			@RequestParam(value = "modelId") Long modelId) {
		UserTokenUtil.needLogin(userId);
		modeService.userCollectionModel(userId, modelId);
		return JsonResult.succResult();
	}

	@GetMapping("/model/list")
	@ApiOperation("用户收藏模型列表")
	public JsonResult<Page<ModelResponse>> userCollectionModelList(
			@LoginUser Long userId,
			@RequestParam(value = "pageNo", defaultValue = "0") @ApiParam("默认从0开始") Integer pageNo,
			@RequestParam(value = "pageSize") Integer pageSize) {
		UserTokenUtil.needLogin(userId);
		if (pageSize > 100) {
			throw new BOException("max pageSize 100");
		}
		Page<ModelResponse> modelDetailResponsePage = modeService
				.userCollectionModelList(pageNo, pageSize, userId);
		return JsonResult.succResult(modelDetailResponsePage);
	}
}
