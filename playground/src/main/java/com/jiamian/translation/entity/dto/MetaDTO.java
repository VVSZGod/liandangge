package com.jiamian.translation.entity.dto;

import lombok.Data;

/**
 * @ClassName: MetaDTO
 * @Auther: z1115
 * @Date: 2023/3/26 13:36
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class MetaDTO {
    private String qiniuUrl;
    private String modelName;
    private String seed;
    private String sampler;
    private String cfgScale;
    private String steps;
    private String prompt;
    private String negativePrompt;

}
