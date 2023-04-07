package com.jiamian.translation.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("提交问题反馈")
public class WorkOrderReq {

	private String title;

	private String content;

	private String url;

	private String type;

}
