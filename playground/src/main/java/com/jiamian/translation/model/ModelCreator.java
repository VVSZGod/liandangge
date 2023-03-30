package com.jiamian.translation.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 模型作者
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "model_creator")
public class ModelCreator implements Serializable {
	public static final long serialVersionUID = 2917264382147205158L;
	private Long id;
	private Long uid;
	private String username;
	private Long modelId;
	private String image;

	public ModelCreator() {
	}

	public ModelCreator(Long uid, String username, Long modelId, String image) {
		this.uid = uid;
		this.username = username;
		this.modelId = modelId;
		this.image = image;
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

	@Column(name = "uid")
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @return 用户名
	 */
	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return 模型ID
	 */
	@Column(name = "model_id")
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	/**
	 * @return 用户头像
	 */
	@Column(name = "image")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}