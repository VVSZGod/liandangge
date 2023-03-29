package com.jiamian.translation.entity.dto;

import lombok.Data;

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
public class StatusApiDTO {
	private int downloadCount = 0;
	private int favoriteCount = 0;
	private int commentCount = 0;
	private int ratingCount = 0;
	private double rating = 0;
}
