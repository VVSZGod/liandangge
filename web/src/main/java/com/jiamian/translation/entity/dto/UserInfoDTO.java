package com.jiamian.translation.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime lastLoginTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;
}
