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


    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneAreaCode() {
        return phoneAreaCode;
    }

    public void setPhoneAreaCode(String phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
    }
}
