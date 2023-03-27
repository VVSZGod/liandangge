package com.jiamian.translation.entity.response;

import com.jiamian.translation.entity.dto.MetaDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: ModelResponse
 * @Auther: z1115
 * @Date: 2023/3/26 11:12
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ModelDetailResponse {

    private Long id;

    private Long modelId;

    private String name;

    private String type;

    //@JsonFormat(timezone = "yyyy-MM-dd HH:mi:ss")
    private LocalDateTime createDate;

    private String modelUrl;

    private String description;

    private List<MetaDTO> metaDTOList;

    private Integer downloadCount;
    private String rating;
}
