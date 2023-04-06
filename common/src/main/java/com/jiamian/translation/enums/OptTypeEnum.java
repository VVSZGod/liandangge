package com.jiamian.translation.enums;

/**
 * @author DingGuangHui
 * @date 2023/2/13
 */
public enum OptTypeEnum {

	NO_LIMIT(0), POS(1), NEG(2);

	private int code;

	OptTypeEnum(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}
}
