package com.jiamian.translation.dao.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "model_tags")
public class ModelTags implements Serializable {
	public static final long serialVersionUID = 8822112178234209774L;
	private Long id;
	private Long modelId;
	private String tagText;
	private String trainedWords;
	private String baseModel;


	public ModelTags() {
	}

	public ModelTags(
			Long modelId, String tagText, String trainedWords, String baseModel) {
		this.modelId = modelId;
		this.tagText = tagText;
		this.trainedWords = trainedWords;
		this.baseModel = baseModel;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "model_id")
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}


	@Column(name = "tagText")
	public String getTagText() {
		return tagText;
	}

	public void setTagText(String tagText) {
		this.tagText = tagText;
	}


	@Column(name = "trainedWords")
	public String getTrainedWords() {
		return trainedWords;
	}

	public void setTrainedWords(String trainedWords) {
		this.trainedWords = trainedWords;
	}


	@Column(name = "baseModel")
	public String getBaseModel() {
		return baseModel;
	}

	public void setBaseModel(String baseModel) {
		this.baseModel = baseModel;
	}

}