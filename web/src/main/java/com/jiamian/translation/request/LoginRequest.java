package com.jiamian.translation.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ding
 * @date 2020/9/15
 */
@Data
@ApiModel("登录参数模型")
public class LoginRequest {

	private String verificationCode;

	private String phoneNumber;

	private String phoneAreaCode;

	private String passWd;

	private String newPassWd;
}
