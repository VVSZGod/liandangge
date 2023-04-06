package com.jiamian.translation.entity.dto.api

import lombok.Data

/**
 * @ClassName: MetaDTO
 * @Auther: z1115
 * @Date: 2023/3/26 13:36
 * @Description: TODO
 * @Version: 1.0
 */
@Data
class MetaDTO {
    var qiniuUrl: String? = null
    var modelName: String? = null
    var seed: String? = null
    var sampler: String? = null
    var cfgScale: String? = null
    var steps: String? = null
    var prompt: String? = null
    var negativePrompt: String? = null
}