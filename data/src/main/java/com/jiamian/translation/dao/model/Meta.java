package com.jiamian.translation.dao.model;

import javax.persistence.*;
import java.io.Serializable;
import static javax.persistence.GenerationType.IDENTITY;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "meta")
public class Meta implements Serializable {
	public static final long serialVersionUID = 1590122400116897808L;
	private Long id;
	private Long modelId;
	private Long modelVersionId;
	private Long imageid;
	private String imageUrl;
	private String qiniuUrl;
	private String modelName;
	private String seed;
	private String sampler;
	private String cfgScale;
	private String steps;
	private String prompt;
	private String negativePrompt;
	private Integer width;
	private Integer height;

	public Meta() {
	}

	public Meta(Long modelId, Long modelVersionId, Long imageid,
			String imageUrl, String qiniuUrl, String modelName, String seed,
			String sampler, String cfgScale, String steps, String prompt,
			String negativePrompt, Integer width, Integer height) {
		this.modelId = modelId;
		this.modelVersionId = modelVersionId;
		this.imageid = imageid;
		this.imageUrl = imageUrl;
		this.qiniuUrl = qiniuUrl;
		this.modelName = modelName;
		this.seed = seed;
		this.sampler = sampler;
		this.cfgScale = cfgScale;
		this.steps = steps;
		this.prompt = prompt;
		this.negativePrompt = negativePrompt;
		this.width = width;
		this.height = height;
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

	@Column(name = "model_version_id")
	public Long getModelVersionId() {
		return modelVersionId;
	}

	public void setModelVersionId(Long modelVersionId) {
		this.modelVersionId = modelVersionId;
	}

	@Column(name = "imageid")
	public Long getImageid() {
		return imageid;
	}

	public void setImageid(Long imageid) {
		this.imageid = imageid;
	}

	@Column(name = "image_url")
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name = "qiniu_url")
	public String getQiniuUrl() {
		return qiniuUrl;
	}

	public void setQiniuUrl(String qiniuUrl) {
		this.qiniuUrl = qiniuUrl;
	}

	@Column(name = "model_name")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Column(name = "seed")
	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	@Column(name = "sampler")
	public String getSampler() {
		return sampler;
	}

	public void setSampler(String sampler) {
		this.sampler = sampler;
	}

	@Column(name = "cfgScale")
	public String getCfgScale() {
		return cfgScale;
	}

	public void setCfgScale(String cfgScale) {
		this.cfgScale = cfgScale;
	}

	@Column(name = "steps")
	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	@Column(name = "prompt")
	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	@Column(name = "negativePrompt")
	public String getNegativePrompt() {
		return negativePrompt;
	}

	public void setNegativePrompt(String negativePrompt) {
		this.negativePrompt = negativePrompt;
	}

	@Column(name = "width")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "height")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

}