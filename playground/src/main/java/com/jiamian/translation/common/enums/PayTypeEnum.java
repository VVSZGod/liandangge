package com.jiamian.translation.common.enums;

/**
 * @author DingGuangHui
 * @date 2022/11/9
 */
public enum PayTypeEnum {
	ALI(1, "ali"),

	WX(2, "weixin");

	private Integer value;

	private String desc;

	PayTypeEnum(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer value() {
		return value;
	}
}
