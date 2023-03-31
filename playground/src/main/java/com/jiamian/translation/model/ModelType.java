package com.jiamian.translation.model;

import javax.persistence.*;
import java.io.Serializable;
import static javax.persistence.GenerationType.IDENTITY;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "model_type")
public class ModelType implements Serializable {
	public static final long serialVersionUID = 5237201704989182216L;
	private Integer id;
	private String type;
	private String showType;
	private Integer status;
	private Integer sortKey;
	private Integer showStatus;

	public ModelType() {
	}

	public ModelType(String type, String showType, Integer status,
			Integer sortKey, Integer showStatus) {
		this.type = type;
		this.showType = showType;
		this.status = status;
		this.sortKey = sortKey;
		this.showStatus = showStatus;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "show_type")
	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "sort_key")
	public Integer getSortKey() {
		return sortKey;
	}

	public void setSortKey(Integer sortKey) {
		this.sortKey = sortKey;
	}

	@Column(name = "show_status")
	public Integer getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(Integer showStatus) {
		this.showStatus = showStatus;
	}

}