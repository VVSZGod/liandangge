package com.jiamian.translation.entity.dto;

import lombok.Data;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Data
public class UserInfoDTO {
	private Long userId;
	private String userName;
	private Integer gender;
	private String phoneNumber;
	private String avatarUrl;
}
