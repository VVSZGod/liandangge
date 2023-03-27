package com.jiamian.translation.common.entity.dto;

public class ShortMessage {

    private String mobileNo;

    private String company;

    private String content;

    public ShortMessage(String mobileNo, String company, String content) {
        this.mobileNo = mobileNo;
        this.company = company;
        this.content = content;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}