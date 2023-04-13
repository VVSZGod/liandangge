package com.jiamian.translation.entity;

/**
 * 机器审核结果枚举
 *
 * @author ding
 * @date 2020/6/22
 */
public enum MachineCheckResultEnum {
	/**
	 * 机器审核结果
	 */
	PASS(1, "通过"), NO_PASS(2, "未通过"), FAIL(4, "请求异常"), PENDING(0, "待审核");

	private Integer code;
	private String desc;

	MachineCheckResultEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getCode() {
		return code;
	}
}