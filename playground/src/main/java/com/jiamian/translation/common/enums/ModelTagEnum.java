package com.jiamian.translation.common.enums;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author DingGuangHui
 * @date 2023/2/14
 */
public enum ModelTagEnum {
	/**
	 * * 顺序重要, 业务层面需要判断主题是否是肖像, 来确定表情的比重
	 */
	CHINESE("CHINESE", "中国风"), ANIME("ANIME", "动漫"), CHARACTER("CHARACTER",
			"人物"), VIDEO_GAME("VIDEO GAME", "电子游戏"), ILLUSTRATION(
					"ILLUSTRATION", "插画"), FANTASY("FANTASY", "幻想"),

	PHOTOGRAPHY("PHOTOGRAPHY", "摄影"), LANDSCAPES("LANDSCAPES",
			"风景"), ARCHITECTURS("ARCHITECTURS",
					"建筑"), SCIFI("SCIFI", "科幻"), RETRO("RETRO", "复古的");

	private String tag;
	private String cnDescribe;

	ModelTagEnum(String tag, String cnDescribe) {
		this.tag = tag;
		this.cnDescribe = cnDescribe;
	}

	public String tag() {
		return tag;
	}

	public String cnDescribe() {
		return cnDescribe;
	}

	// 需要自动分配权重的tag
	public static List<ModelTagEnum> remainderTags() {
		return Lists.newArrayList(CHINESE, ANIME, CHARACTER, VIDEO_GAME,
				ILLUSTRATION, FANTASY, PHOTOGRAPHY, LANDSCAPES, ARCHITECTURS,
				SCIFI, RETRO);
	}
}
