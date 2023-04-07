package com.jiamian.translation.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: ModelResponse
 * @Auther: z1115
 * @Date: 2023/3/26 11:12
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ModelResponse {

	private Long id;

	private Long modelId;

	private Long modelVersionId;

	private String name;

	private String type;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createDate;

	private String imageUrl;

	private Integer downloadCount;

	private String rating;

	@ApiModelProperty(value = "")
	private String creatorUserName;
	@ApiModelProperty(value = "")
	private String creatorHeadThumb;
	@ApiModelProperty(value = "")
	private String creatorLink;

	private boolean isCollectStat;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime collectDate;

	private Integer imageWidth;

	private Integer imageHeight;

	private Integer chinese;

	private Integer recommend;

	private Integer status;

	private String version;

	private boolean modelClaimStat;

}
