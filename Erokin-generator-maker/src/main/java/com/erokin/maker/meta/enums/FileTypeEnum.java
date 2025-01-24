package com.erokin.maker.meta.enums;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {
    //枚举一般写入两个参数，一个描述，一个枚举
    GROUP("文件组","group"),
    DIR("目录","dir"),
    FILE("文件","file");


    private final String text;
    private final  String value;
    FileTypeEnum(String text, String value){
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
