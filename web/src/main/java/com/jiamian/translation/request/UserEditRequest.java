package com.jiamian.translation.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ding
 * @date 2020/9/15
 */
@Data
@ApiModel("修改用户参数模型")
public class UserEditRequest {

    @ApiModelProperty(value = "昵称")
    private String userName;
    
    @ApiModelProperty(value = "性别(0 未知  1 男 2女)")
    private Integer gender;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;
    @ApiModelProperty(value = "是否公开配方(1/0)")
    private Integer publicParam;
    @ApiModelProperty(value = "是否分享作品(1/0)")
    private Integer shareWork;

}
