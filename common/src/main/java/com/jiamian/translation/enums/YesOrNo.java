package com.jiamian.translation.enums;

public enum YesOrNo {

	NO(0, "否"),

	YES(1, "是");

	private Integer value;

	private String desc;

	YesOrNo(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer value() {
		return value;
	}

}