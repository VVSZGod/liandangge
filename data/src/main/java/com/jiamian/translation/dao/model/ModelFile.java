package com.jiamian.translation.dao.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "model_file")
public class ModelFile implements Serializable {
	public static final long serialVersionUID = 7810894716151576037L;
	private Long id;
	private Long modelId;
	private Long modelVersionId;
	private String fileName;
	private Long fileId;
	private String fileSize;
	private String hashAutov1;
	private String hashAutov2;
	private String hashSha256;
	private String hashCrc32;
	private String hashBlake3;
	private String downloadUrl;

	public ModelFile() {
	}

	public ModelFile(Long modelId, Long modelVersionId, String fileName,
			Long fileId, String fileSize, String hashAutov1, String hashAutov2,
			String hashSha256, String hashCrc32, String hashBlake3,
			String downloadUrl) {
		this.modelId = modelId;
		this.modelVersionId = modelVersionId;
		this.fileName = fileName;
		this.fileId = fileId;
		this.fileSize = fileSize;
		this.hashAutov1 = hashAutov1;
		this.hashAutov2 = hashAutov2;
		this.hashSha256 = hashSha256;
		this.hashCrc32 = hashCrc32;
		this.hashBlake3 = hashBlake3;
		this.downloadUrl = downloadUrl;
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

	@Column(name = "file_name")
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "file_id")
	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "file_size")
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	@Column(name = "hash_autov1")
	public String getHashAutov1() {
		return hashAutov1;
	}

	public void setHashAutov1(String hashAutov1) {
		this.hashAutov1 = hashAutov1;
	}

	@Column(name = "hash_autov2")
	public String getHashAutov2() {
		return hashAutov2;
	}

	public void setHashAutov2(String hashAutov2) {
		this.hashAutov2 = hashAutov2;
	}

	@Column(name = "hash_sha256")
	public String getHashSha256() {
		return hashSha256;
	}

	public void setHashSha256(String hashSha256) {
		this.hashSha256 = hashSha256;
	}

	@Column(name = "hash_crc32")
	public String getHashCrc32() {
		return hashCrc32;
	}

	public void setHashCrc32(String hashCrc32) {
		this.hashCrc32 = hashCrc32;
	}

	@Column(name = "hash_blake3")
	public String getHashBlake3() {
		return hashBlake3;
	}

	public void setHashBlake3(String hashBlake3) {
		this.hashBlake3 = hashBlake3;
	}

	@Column(name = "download_url")
	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

}