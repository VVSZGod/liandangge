package com.jiamian.translation.common.enums;

/**
 * @author DingGuangHui
 * @date 2022/11/8
 */
public enum OrderStatusEnum {
	/**
	 * 
	 */
	PRE_PAY(0, "预支付", "paied"),

	PAY_SUCCESS(1, "支付成功", "success"),

	PAY_FAIL(2, "支付失败", "fail");

	private Integer value;

	private String desc;

	private String code;

	OrderStatusEnum(Integer value, String desc, String code) {
		this.value = value;
		this.desc = desc;
		this.code = code;
	}

	public Integer value() {
		return value;
	}

	public String code() {
		return code;
	}

}
