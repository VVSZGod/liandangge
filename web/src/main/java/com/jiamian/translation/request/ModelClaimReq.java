package com.jiamian.translation.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("提交模型认领")
public class ModelClaimReq {

	private String title;

	private String content;

	private String url;

	private Long modelId;

}
