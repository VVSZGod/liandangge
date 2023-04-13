package com.jiamian.translation.enums;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author DingGuangHui
 * @date 2023/2/14
 */
public enum ProduceTagEnum {
	/**
	 * * 顺序重要, 业务层面需要判断主题是否是肖像, 来确定表情的比重
	 */
	Conceptually("conceptually","作品造诣"),
	ImgType("imageType","作品材质"),
	Influence("influence","作品主题"),
	Subject("subject","画面主体"),
	Mood("mood","表情"),
	Action("action","动作"),
	
	
	Location("location","位置"),
	Color("color","颜色"),
	ArtMovement("art movement","艺术思潮"),
	Direction("direction","作品载体"),
	Artists("artists","艺术家");

	private String tag;
	private String cnDescribe;

	ProduceTagEnum(String tag, String cnDescribe) {
		this.tag = tag;
		this.cnDescribe = cnDescribe;
	}

	public String tag() {
		return tag;
	}

	// 需要自动分配权重的tag
	public static List<ProduceTagEnum> remainderTags() {
		return Lists.newArrayList(Location, Color, ArtMovement, Direction,
				Artists);
	}
}
