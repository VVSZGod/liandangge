package com.jiamian.translation.entity;

/**
 * @author ding
 * @date 2020/6/19
 */
public class MachineCheckResult {

	private MachineCheckResultEnum rsEnum;

	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public MachineCheckResultEnum getRsEnum() {
		return rsEnum;
	}

	public void setRsEnum(MachineCheckResultEnum rsEnum) {
		this.rsEnum = rsEnum;
	}

	public MachineCheckResult() {
	}

	public MachineCheckResult(MachineCheckResultEnum rsEnum, String msg) {
		this.rsEnum = rsEnum;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "{\"MachineCheckResult\":{" + "\"rsEnum\":" + rsEnum
				+ ",\"msg\":\"" + msg + '\"' + "}}";

	}
}
