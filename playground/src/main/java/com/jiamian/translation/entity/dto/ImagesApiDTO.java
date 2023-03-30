package com.jiamian.translation.entity.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
public class ImagesApiDTO {

	private String url = "";
	private boolean nsfw = false;
	private int width;
	private int height;
	private String hash = "";
	private int userId;
	private String generationProcess = "";
	private boolean needsReview = false;
	private String scannedAt = "";
	private List<String> tags = Lists.newArrayList();
	private MetaApiDTO meta;
}
