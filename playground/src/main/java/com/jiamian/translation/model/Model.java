package com.jiamian.translation.model;

import javax.persistence.*;
import java.io.Serializable;
import static javax.persistence.GenerationType.IDENTITY;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

/**
 * 模型表
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "model")
public class Model implements Serializable {
	public static final long serialVersionUID = 6071459448611999698L;
	private Long id;
	private Long modelId;
	private String name;
	private String type;
	private Long modelVersionId;
	private String modelUrl;
	private String nsfw;
	private Integer status;
	private String description;
	private LocalDateTime createDate;
	private String aliUrl;
	private String aliPwd;
	private Integer downloadCount;
	private Integer favoriteCount;
	private Integer commentCount;
	private Integer ratingCount;
	private String rating;
	private Integer ldgDownloadCount;
	private String version;

	public Model() {
	}

	public Model(Long modelId, String name, String type, Long modelVersionId,
			String modelUrl, String nsfw, Integer status, String description,
			LocalDateTime createDate, String aliUrl, String aliPwd,
			Integer downloadCount, Integer favoriteCount, Integer commentCount,
			Integer ratingCount, String rating, Integer ldgDownloadCount,
			String version) {
		this.modelId = modelId;
		this.name = name;
		this.type = type;
		this.modelVersionId = modelVersionId;
		this.modelUrl = modelUrl;
		this.nsfw = nsfw;
		this.status = status;
		this.description = description;
		this.createDate = createDate;
		this.aliUrl = aliUrl;
		this.aliPwd = aliPwd;
		this.downloadCount = downloadCount;
		this.favoriteCount = favoriteCount;
		this.commentCount = commentCount;
		this.ratingCount = ratingCount;
		this.rating = rating;
		this.ldgDownloadCount = ldgDownloadCount;
		this.version = version;
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

	/**
	 * @return 模型名字
	 */
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 类型
	 */
	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "model_version_id")
	public Long getModelVersionId() {
		return modelVersionId;
	}

	public void setModelVersionId(Long modelVersionId) {
		this.modelVersionId = modelVersionId;
	}

	@Column(name = "model_url")
	public String getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	/**
	 * @return nsfw
	 */
	@Column(name = "nsfw")
	public String getNsfw() {
		return nsfw;
	}

	public void setNsfw(String nsfw) {
		this.nsfw = nsfw;
	}

	/**
	 * @return 任务是否启动,默认未启动 default : 1
	 */
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "create_date")
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	@Column(name = "ali_url")
	public String getAliUrl() {
		return aliUrl;
	}

	public void setAliUrl(String aliUrl) {
		this.aliUrl = aliUrl;
	}

	@Column(name = "ali_pwd")
	public String getAliPwd() {
		return aliPwd;
	}

	public void setAliPwd(String aliPwd) {
		this.aliPwd = aliPwd;
	}

	/**
	 * @return 下载量 default : 0
	 */
	@Column(name = "downloadCount")
	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	@Column(name = "favoriteCount")
	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	@Column(name = "commentCount")
	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	@Column(name = "ratingCount")
	public Integer getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(Integer ratingCount) {
		this.ratingCount = ratingCount;
	}

	@Column(name = "rating")
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Column(name = "ldg_download_count")
	public Integer getLdgDownloadCount() {
		return ldgDownloadCount;
	}

	public void setLdgDownloadCount(Integer ldgDownloadCount) {
		this.ldgDownloadCount = ldgDownloadCount;
	}

	@Column(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}