package com.jiamian.translation.entity.response;

import java.time.LocalDateTime;
import java.util.List;

import com.jiamian.translation.entity.dto.MetaDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ModelResponse
 * @Auther: z1115
 * @Date: 2023/3/26 11:12
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@ApiModel(value = "模型详情")
public class ModelDetailResponse {

	private Long id;

	private Long modelId;

	private String name;

	private String type;

	private LocalDateTime createDate;

	private String modelUrl;

	private String description;

	private List<MetaDTO> metaDTOList;

	private Integer downloadCount;
	private String rating;
	@ApiModelProperty(value = "")
	private String creatorUserName;
	@ApiModelProperty(value = "")
	private String creatorHeadThumb;
	@ApiModelProperty(value = "")
	private String creatorLink;

	private List<String> tags;
	private List<String> trainedWords;
	private String baseModel;

}
