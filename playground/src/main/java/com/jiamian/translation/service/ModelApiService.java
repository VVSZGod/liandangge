package com.jiamian.translation.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jiamian.translation.dao.repository.MetaRepository;
import com.jiamian.translation.dao.repository.ModelCreatorRepository;
import com.jiamian.translation.dao.repository.ModelRepository;
import com.jiamian.translation.dao.repository.ModelTagsRepository;
import com.jiamian.translation.entity.dto.CreatorApiDTO;
import com.jiamian.translation.entity.dto.FilesApiDTO;
import com.jiamian.translation.entity.dto.ImagesApiDTO;
import com.jiamian.translation.entity.dto.MetaApiDTO;
import com.jiamian.translation.entity.dto.ModelApiDTO;
import com.jiamian.translation.entity.dto.ModelVersionsApiDTO;
import com.jiamian.translation.entity.dto.StatusApiDTO;
import com.jiamian.translation.model.Meta;
import com.jiamian.translation.model.Model;
import com.jiamian.translation.model.ModelCreator;
import com.jiamian.translation.model.ModelTags;
import com.jiamian.translation.util.DateUtil;
import com.jiamian.translation.util.QiniuUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Service
public class ModelApiService {
	@Autowired
	private ModelRepository modelRepository;
	@Autowired
	private MetaRepository metaRepository;
	@Autowired
	private ModelCreatorRepository modelCreatorRepository;
	@Autowired
	private ModelTagsRepository modelTagsRepository;

	@Autowired
	private QiNiuService qiNiuService;

	private static final String MODELS_JSON_FILE_NAME = "%s_models.json";

	public List<ModelApiDTO> uploadQiniuModelsJson() {
		List<ModelApiDTO> models = Lists.newArrayList();

		Model model = new Model();
		model.setStatus(1);
		int pageSize = 10;
		int pageIdx = 0;

		List<Model> dbModels;
		List<ModelApiDTO> item = Lists.newArrayList();

		do {
			Page<Model> p = modelRepository.findAll(Example.of(model), PageRequest.of(pageIdx, pageSize, Sort.Direction.ASC,
					"id"));
			dbModels = p.getContent();
			pageIdx++;


			for (Model dbModel : dbModels) {
				ModelApiDTO modelApiDTO = new ModelApiDTO();
				modelApiDTO.setId(dbModel.getId());
				modelApiDTO.setName(dbModel.getName());
				modelApiDTO.setDescription(dbModel.getDescription());
				modelApiDTO.setType(dbModel.getType());
				String trainedWords = "";
				String baseModel = "";

				StatusApiDTO statusApiDTO = new StatusApiDTO();
				statusApiDTO.setDownloadCount(dbModel.getDownloadCount());
				statusApiDTO.setFavoriteCount(dbModel.getFavoriteCount());
				statusApiDTO.setCommentCount(dbModel.getCommentCount());
				statusApiDTO.setRatingCount(dbModel.getRatingCount());
				statusApiDTO.setRating(Double.parseDouble(dbModel.getRating()));
				modelApiDTO.setStats(statusApiDTO);

				CreatorApiDTO creatorApiDTO = new CreatorApiDTO();
				ModelCreator creator = modelCreatorRepository.findByModelId(dbModel.getModelId());
				if (ObjectUtils.isNotEmpty(creator)) {
					creatorApiDTO.setUsername(creator.getUsername());
					creator.setImage("");
				}
				modelApiDTO.setCreator(creatorApiDTO);


				List<ModelTags> modelTags = modelTagsRepository.findByModelId(dbModel.getModelId());
				if (CollectionUtil.isNotEmpty(modelTags)) {
					ModelTags modelT = modelTags.get(0);

					String tags = modelT.getTagText();
					trainedWords = ObjectUtils.isNotEmpty(modelT.getTrainedWords()) ? modelT.getTrainedWords() : "";
					baseModel = modelT.getBaseModel();
					modelApiDTO.setTags(Arrays.asList(tags.split(",")));
				}


				ModelVersionsApiDTO mvDTO = new ModelVersionsApiDTO();
				mvDTO.setDownloadPwd(dbModel.getAliPwd());
				mvDTO.setDownloadUrl(dbModel.getAliUrl());
				mvDTO.setId(dbModel.getId());
				mvDTO.setModelId(dbModel.getModelId());
				mvDTO.setName(dbModel.getName());
				mvDTO.setCreateAt(dbModel.getCreateDate());
				mvDTO.setUpdatedAt(dbModel.getCreateDate());
				mvDTO.setTrainedWords(Lists.newArrayList(trainedWords.split(",")));
				mvDTO.setBaseModel(baseModel);

				FilesApiDTO filesApiDTO = new FilesApiDTO();
				filesApiDTO.setDownloadUrl(dbModel.getAliUrl());
				mvDTO.setFiles(Lists.newArrayList(filesApiDTO));


				List<Meta> metas = metaRepository.findByModelId(dbModel.getModelId());
				List<ImagesApiDTO> images = Lists.newArrayList();

				if (CollectionUtil.isNotEmpty(metas)) {
					for (Meta meta : metas) {
						ImagesApiDTO imagesApiDTO = new ImagesApiDTO();
						imagesApiDTO.setUrl(meta.getQiniuUrl());
						imagesApiDTO.setNsfw(false);

						JSONObject imgInfo = QiniuUtil.getImgInfo(meta.getQiniuUrl());
						Integer width = imgInfo.getInteger("width");
						Integer height = imgInfo.getInteger("height");
						imagesApiDTO.setWidth(width);
						imagesApiDTO.setHeight(height);

						MetaApiDTO metaApiDTO = new MetaApiDTO();

						metaApiDTO.setSize(width + "x" + height);
						metaApiDTO.setSeed(Long.parseLong(meta.getSeed()));
						metaApiDTO.setModel(dbModel.getName());
						metaApiDTO.setSteps(Integer.parseInt(meta.getSteps()));
						metaApiDTO.setPrompt(meta.getPrompt());
						metaApiDTO.setSampler(meta.getSampler());
						metaApiDTO.setCfgScale(Double.parseDouble(meta.getCfgScale()));
						metaApiDTO.setNegativePrompt(meta.getNegativePrompt());

						imagesApiDTO.setMeta(metaApiDTO);
						images.add(imagesApiDTO);
					}
				}

				mvDTO.setImages(images);
				modelApiDTO.setModelVersions(Lists.newArrayList(mvDTO));
				item.add(modelApiDTO);
			}
			break;
		} while (CollectionUtil.isNotEmpty(dbModels));

		ApiResp apiResp = new ApiResp(item);

		qiNiuService.uploadFile(new ByteArrayInputStream(JSON.toJSONString(apiResp).getBytes()),
				String.format(MODELS_JSON_FILE_NAME, DateUtil.formatYYYYMMDD()));
		return Lists.newArrayList();
	}

	class ApiResp {
		List<ModelApiDTO> items = Lists.newArrayList();

		public ApiResp() {
		}

		public ApiResp(List<ModelApiDTO> items) {
			this.items = items;
		}

		public List<ModelApiDTO> getItems() {
			return items;
		}

		public void setItems(List<ModelApiDTO> items) {
			this.items = items;
		}
	}
}
