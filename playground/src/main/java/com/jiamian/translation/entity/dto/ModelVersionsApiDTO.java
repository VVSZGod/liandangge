package com.jiamian.translation.entity.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
public class ModelVersionsApiDTO {

	private long id = 0;
	private long modelId;
	private String name;
	private LocalDateTime createAt;
	private LocalDateTime updatedAt ;
	private List<String> trainedWords = Lists.newArrayList();
	private String baseModel;
	private int earlyAccessTimeFrame = 0;
	private String description = "";
	private String downloadUrl = "";
	private List<FilesApiDTO> files;
	private List<ImagesApiDTO> images;

}
