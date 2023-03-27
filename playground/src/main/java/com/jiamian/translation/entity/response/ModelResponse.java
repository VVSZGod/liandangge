package com.jiamian.translation.entity.response;

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

    private String name;

    private String type;

    //@JsonFormat(timezone = "yyyy-MM-dd HH:mi:ss")
    private LocalDateTime createDate;

    private String imageUrl;

    private String description;

}
