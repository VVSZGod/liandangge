package com.jiamian.translation.service

import cn.hutool.core.collection.CollectionUtil
import com.google.common.collect.Lists
import com.jiamian.translation.dao.repository.MetaRepository
import com.jiamian.translation.dao.repository.ModelCreatorRepository
import com.jiamian.translation.dao.repository.ModelRepository
import com.jiamian.translation.dao.repository.ModelTagsRepository
import com.jiamian.translation.entity.dto.api.*
import com.jiamian.translation.model.Model
import com.jiamian.translation.util.QiniuUtil.getImgInfo
import org.apache.commons.lang3.ObjectUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Service
class ModelApiService {
    @Autowired
    private lateinit var modelRepository: ModelRepository

    @Autowired
    private lateinit var metaRepository: MetaRepository

    @Autowired
    private lateinit var modelCreatorRepository: ModelCreatorRepository

    @Autowired
    private lateinit var modelTagsRepository: ModelTagsRepository


    /**
     * 当前model信息构造json上传七牛云
     */
    fun pageModelApi(pageSize: Int, pageIdx: Int): ApiResp {
        val example = Model()
        example.status = 1

        val item: MutableList<ModelApiDTO> = Lists.newArrayList()

        val p = modelRepository.findAll(Example.of(example),
                PageRequest.of(pageIdx, pageSize, Sort.Direction.ASC,
                        "id"))

        for (dbModel in p.content) {
            val modelApiDTO = ModelApiDTO(dbModel.id)
            modelApiDTO.name = dbModel.name
            modelApiDTO.description = dbModel.description
            modelApiDTO.type = dbModel.type
            var trainedWords = ""
            var baseModel = ""
            val downloadUrl = String.format(MODEL_DETAIL_URL,
                    dbModel.modelId)


            val statusApiDTO = StatusApiDTO()
            statusApiDTO.downloadCount = dbModel.downloadCount
            statusApiDTO.favoriteCount = dbModel.favoriteCount
            statusApiDTO.commentCount = dbModel.commentCount
            statusApiDTO.ratingCount = dbModel.ratingCount
            statusApiDTO.rating = dbModel.rating.toDouble()
            modelApiDTO.stats = statusApiDTO


            val creatorApiDTO = CreatorApiDTO()
            val creators = modelCreatorRepository.findByModelId(dbModel.modelId)
            if (CollectionUtil.isNotEmpty(creators)) {
                creatorApiDTO.username = creators[0]!!.username
                creatorApiDTO.image = creators[0]!!.image
            }
            modelApiDTO.creator = creatorApiDTO


            val modelTags = modelTagsRepository
                    .findByModelId(dbModel.modelId)
            if (CollectionUtil.isNotEmpty(modelTags)) {
                val modelT = modelTags[0]
                val tags = modelT!!.tagText
                trainedWords = if (ObjectUtils.isNotEmpty(
                                modelT.trainedWords)) modelT.trainedWords else ""
                baseModel = modelT.baseModel
                modelApiDTO.tags = Arrays.asList(*tags.split(",".toRegex()).toTypedArray())
            }


            val mvDTO = ModelVersionsApiDTO()
            mvDTO.downloadUrl = downloadUrl
            mvDTO.id = dbModel.id
            mvDTO.modelId = dbModel.modelId
            mvDTO.name = dbModel.name
            mvDTO.createAt = dbModel.createDate
            mvDTO.updatedAt = dbModel.createDate
            mvDTO.trainedWords = Lists.newArrayList(*trainedWords.split(",".toRegex()).toTypedArray())
            mvDTO.baseModel = baseModel
            val filesApiDTO = FilesApiDTO()
            filesApiDTO.downloadUrl = downloadUrl
            mvDTO.files = Lists.newArrayList(filesApiDTO)
            val metas = metaRepository
                    .findByModelId(dbModel.modelId)
            val images: MutableList<ImagesApiDTO> = Lists.newArrayList()
            if (CollectionUtil.isNotEmpty(metas)) {
                for (meta in metas) {
                    val imagesApiDTO = ImagesApiDTO()
                    imagesApiDTO.url = meta!!.qiniuUrl + "-mshalf"
                    imagesApiDTO.nsfw = false
                    val imgInfo = getImgInfo(meta.qiniuUrl)
                    val width = imgInfo.getInteger("width")
                    val height = imgInfo.getInteger("height")
                    imagesApiDTO.width = width
                    imagesApiDTO.height = height
                    val metaApiDTO = MetaApiDTO()
                    metaApiDTO.Size = width.toString() + "x" + height
                    metaApiDTO.seed = meta.seed.toLong()
                    metaApiDTO.Model = dbModel.name
                    metaApiDTO.steps = meta.steps.toInt()
                    metaApiDTO.prompt = meta.prompt
                    metaApiDTO.sampler = meta.sampler
                    metaApiDTO.cfgScale = meta.cfgScale.toDouble()
                    metaApiDTO.negativePrompt = meta.negativePrompt
                    imagesApiDTO.meta = metaApiDTO
                    images.add(imagesApiDTO)
                }
            }
            mvDTO.images = images
            modelApiDTO.modelVersions = Lists.newArrayList(mvDTO)
            item.add(modelApiDTO)
        }

        return ApiResp(item, MetaDataApiDTO(p.totalElements, pageIdx, pageSize, p.totalPages))
    }

    inner class ApiResp {
        var items: List<ModelApiDTO> = Lists.newArrayList()
        var metadata: MetaDataApiDTO = MetaDataApiDTO(0, 0, 0, 0)

        constructor() {}
        constructor(items: List<ModelApiDTO>, metadata: MetaDataApiDTO) {
            this.items = items
            this.metadata = metadata
        }
    }

    companion object {
        //
        private const val MODEL_DETAIL_URL = "https://models.paomiantv.cn/models/Detail?id=%s"
    }
}