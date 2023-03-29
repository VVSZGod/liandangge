package com.jiamian.translation.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
public class MetaApiDTO {
	@JSONField(name = "Size")
	private String Size = "";
	private long seed ;
	@JSONField(name = "Model")
	private String Model = "";
	private int steps ;
	private String prompt= "";
	private String sampler = "";
	private double cfgScale;
	private List resources = Lists.newArrayList();
	private String negativePrompt="";
	private boolean needsReview = false;
	private String scannedAt = "";
}
