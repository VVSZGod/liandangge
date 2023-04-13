package com.jiamian.translation.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginUserResponse {

	private Long userId;
	private String userName;
	private Integer gender;
	private String phoneNumber;
	private String avatarUrl;
	private String token;

	private boolean registerStat;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime lastLoginTime;
}
