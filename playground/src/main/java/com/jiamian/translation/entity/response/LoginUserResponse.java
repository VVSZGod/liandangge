package com.jiamian.translation.entity.response;

import lombok.Data;

@Data
public class LoginUserResponse {

	private Long userId;
	private String userName;
	private Integer gender;
	private String phoneNumber;
	private String avatarUrl;
	private String token;

	private boolean registerStat;
}
