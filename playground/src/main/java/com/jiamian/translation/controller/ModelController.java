package com.jiamian.translation.controller;

import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.jiamian.translation.common.enums.ModelTagEnum;
import com.jiamian.translation.entity.response.ModelTagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiamian.translation.annotation.LoginUser;
import com.jiamian.translation.common.entity.JsonResult;
import com.jiamian.translation.common.entity.Page;
import com.jiamian.translation.entity.response.ModelDetailResponse;
import com.jiamian.translation.entity.response.ModelResponse;
import com.jiamian.translation.entity.response.ModelTypeResponse;
import com.jiamian.translation.service.ModeServiceImpl;
import com.jiamian.translation.service.ModelTypeServiceImpl;
import com.jiamian.translation.util.UserTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: ModelController
 * @Auther: z1115
 * @Date: 2023/3/26 11:25
 * @Description: TODO
 * @Version: 1.0
 */
@RestController
@RequestMapping("/model")
@Api(tags = "模型")
public class ModelController {

	@Autowired
	private ModeServiceImpl modeService;

	@Autowired
	private ModelTypeServiceImpl modelTypeService;

	@GetMapping("/list")
	@ApiOperation("模型列表")
	public JsonResult<Page<ModelResponse>> modelList(
			@RequestParam(value = "pageNo", defaultValue = "0") @ApiParam("默认从0开始") Integer pageNo,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "sortType", defaultValue = "1") Integer sortType) {
		Page<ModelResponse> modelResponsePage = modeService.pageModel(pageNo,
				pageSize, key, type, sortType);
		return JsonResult.succResult(modelResponsePage);
	}

	@GetMapping("/detail")
	@ApiOperation("模型详情")
	public JsonResult<ModelDetailResponse> modelDetail(@LoginUser Long userId,
			@RequestParam(value = "modelId") Integer modelId) {
		ModelDetailResponse modelDetailResponse = modeService
				.modelDetail(userId, modelId.longValue());
		return JsonResult.succResult(modelDetailResponse);
	}

	@GetMapping("/url")
	@ApiOperation("模型链接下载11")
	public JsonResult<Map<String, String>> getModelUrl(@LoginUser Long userId,
			@RequestParam(value = "modelId") Integer modelId) {
		UserTokenUtil.needLogin(userId);
		return JsonResult.succResult(modeService.getModelUrl(modelId));
	}

	@GetMapping("/count")
	@ApiOperation("模型总数量和已经上传数量")
	public JsonResult<Map<String, Integer>> getModelCount() {
		return JsonResult.succResult(modeService.getModelCount());
	}

	@GetMapping("/list/type")
	@ApiOperation("模型类型集合")
	public JsonResult<List<ModelTypeResponse>> modelTypeResponseList() {
		List<ModelTypeResponse> modelTypeResponses = modelTypeService
				.modelTypeResponseList();
		return JsonResult.succResult(modelTypeResponses);
	}

	@GetMapping("/list/tag")
	@ApiOperation("模型TAG集合")
	public JsonResult<List<ModelTagResponse>> modelTagResponseList() {
		List<ModelTagEnum> modelTagEnums = ModelTagEnum.remainderTags();
		List<ModelTagResponse> collect = modelTagEnums.stream()
				.map(modelTagEnum -> {
					ModelTagResponse modelTagResponse = new ModelTagResponse();
					modelTagResponse.setTag(modelTagEnum.tag());
					modelTagResponse.setCnDescribe(modelTagEnum.cnDescribe());
					return modelTagResponse;
				}).collect(Collectors.toList());
		return JsonResult.succResult(collect);
	}

	@GetMapping("/list/by/tag")
	@ApiOperation("根据tag查找模型列表")
	public JsonResult<Page<ModelResponse>> modelByTagList(
			@RequestParam(value = "pageNo", defaultValue = "0") @ApiParam("默认从0开始") Integer pageNo,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "sortType", defaultValue = "1") Integer sortType) {
//		Page<ModelResponse> modelResponsePage = modeService.modelByTagList(pageNo,
//				pageSize, key, type, sortType);
		return JsonResult.succResult(null);
	}
}
