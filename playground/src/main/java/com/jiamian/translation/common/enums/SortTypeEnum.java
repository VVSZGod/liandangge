package com.jiamian.translation.common.enums;

public enum SortTypeEnum {
    DOWN_COUNT(1, "下载量"),
    TIME(2, "时间排序"),
    RATING(3, "评分排序");

    private Integer value;

    private String desc;

    SortTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer value() {
        return value;
    }
}
