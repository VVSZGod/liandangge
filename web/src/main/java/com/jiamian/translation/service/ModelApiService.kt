package com.jiamian.translation.service

import cn.hutool.core.collection.CollectionUtil
import com.google.common.collect.Lists
import com.jiamian.translation.dao.model.Model
import com.jiamian.translation.dao.model.ModelFile
import com.jiamian.translation.dao.repository.*
import com.jiamian.translation.entity.dto.api.*
import org.apache.commons.lang3.ObjectUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Service
class ModelApiService {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var modelRepository: ModelRepository

    @Autowired
    private lateinit var metaRepository: MetaRepository

    @Autowired
    private lateinit var modelCreatorRepository: ModelCreatorRepository

    @Autowired
    private lateinit var modelTagsRepository: ModelTagsRepository

    @Autowired
    private lateinit var modelFileRepository: ModelFileRepository


    /**
     * 当前model信息构造json上传七牛云
     */
    fun pageModelApi(pageSize: Int, pageIdx: Int): ApiResp {

        val specification = Specification { root: Root<Model?>, criteriaQuery: CriteriaQuery<*>?, cb: CriteriaBuilder ->
            val predicates: MutableList<Predicate> = Lists.newArrayList()
            predicates.add(cb.equal(root.get<Any>("status"), 1))

            val modelUrl = root.get<Any>("modelUrl")
            predicates.add(cb.isNotNull(modelUrl))
            predicates.add(cb.not(cb.equal(modelUrl, "")))

            cb.and(*predicates.toTypedArray())
        }


        val item: MutableList<ModelApiDTO> = Lists.newArrayList()

        val p = modelRepository.findAll(
                specification,
                PageRequest.of(pageIdx, pageSize, Sort.Direction.ASC, "id")
        )

        for (dbModel in p.content) {
            val modelApiDTO = ModelApiDTO(dbModel.id)
            modelApiDTO.name = dbModel.name
            modelApiDTO.description = dbModel.description
            modelApiDTO.type = dbModel.type
            var trainedWords = ""
            var baseModel = ""
            val downloadUrl = String.format(
                    MODEL_DETAIL_URL,
                    dbModel.modelId,
                    dbModel.modelVersionId
            )


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
                                modelT.trainedWords
                        )
                ) modelT.trainedWords else ""
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

            modelFileRepository.findByModelIdAndModelVersionId(mvDTO.modelId, mvDTO.id).takeIf {
                it.isPresent
            }?.get()?.let {
                filesApiDTO.name = it.fileName
                filesApiDTO.sizeKB = it.fileSize.takeIf { fileSize -> fileSize.isNotBlank() }?.toDouble()?: 0.0
                filesApiDTO.id = it.fileId
                filesApiDTO.primary = true
                filesApiDTO.hashes = getHashCodeMap(it)
                filesApiDTO.downloadUrl = downloadUrl
            }
            mvDTO.files = Lists.newArrayList(filesApiDTO)


            val metas = metaRepository
                    .findByModelIdAndModelVersionId(dbModel.modelId, dbModel.modelVersionId)
            val images: MutableList<ImagesApiDTO> = Lists.newArrayList()
            if (CollectionUtil.isNotEmpty(metas)) {
                for (meta in metas) {
                    try {
                        val imagesApiDTO = ImagesApiDTO()
                        imagesApiDTO.url = meta!!.qiniuUrl + "?imageView2/2/w/100"
                        imagesApiDTO.nsfw = false
                        val width = meta.width
                        val height = meta.height
                        imagesApiDTO.width = width
                        imagesApiDTO.height = height
                        val metaApiDTO = MetaApiDTO()
                        metaApiDTO.Size = width.toString() + "x" + height
                        metaApiDTO.seed = meta.seed.toLong()
                        metaApiDTO.Model = dbModel.name
                        metaApiDTO.steps = meta.steps.toLong()
                        metaApiDTO.prompt = meta.prompt
                        metaApiDTO.sampler = meta.sampler
                        metaApiDTO.cfgScale = meta.cfgScale.toDouble()
                        metaApiDTO.negativePrompt = meta.negativePrompt
                        imagesApiDTO.meta = metaApiDTO
                        images.add(imagesApiDTO)
                    } catch (e: Exception) {
                        log.error("图片处理异常", e)
                    }

                }
            }
            mvDTO.images = images
            modelApiDTO.modelVersions = Lists.newArrayList(mvDTO)
            item.add(modelApiDTO)
        }

        return ApiResp(item, MetaDataApiDTO(p.totalElements, pageIdx, pageSize, p.totalPages))
    }

    private fun getHashCodeMap(it: ModelFile): MutableMap<String, String> {
        val mutbleMapOf = mutableMapOf<String, String>()
        mutbleMapOf["AutoV1"] = it.hashAutov1
        mutbleMapOf["AutoV2"] = it.hashAutov2
        mutbleMapOf["SHA256"] = it.hashSha256
        mutbleMapOf["CRC32"] = it.hashCrc32
        mutbleMapOf["BLAKE3"] = it.hashBlake3
        return mutbleMapOf
    }

    companion object {
        //
        private const val MODEL_DETAIL_URL = "http://www.liandange.com/models/Detail?id=%s&modelVersionId=%s"
    }
}