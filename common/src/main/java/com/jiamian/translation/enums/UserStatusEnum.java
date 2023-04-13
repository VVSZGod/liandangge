package com.jiamian.translation.enums;

/**
 * @author Ding
 * @date 2022/11/7
 */
public enum UserStatusEnum {
	DISABLE(0, "否"),

	ENABLE(1, "是");

	private Integer value;

	private String desc;

	UserStatusEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer value() {
		return value;
	}
}
