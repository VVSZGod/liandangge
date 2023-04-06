package com.jiamian.translation.entity.response;

import java.util.List;

import com.jiamian.translation.entity.dto.api.MetaDTO;

import io.swagger.annotations.ApiModel;
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
public class ModelDetailResponse extends ModelResponse{


	private List<MetaDTO> metaDTOList;


	private List<String> tags;

	private List<String> trainedWords;

	private String baseModel;

	private String version;


}
