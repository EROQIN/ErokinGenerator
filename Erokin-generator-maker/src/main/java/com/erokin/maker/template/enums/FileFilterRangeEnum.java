package com.erokin.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public enum FileFilterRangeEnum {
    FILE_NAME("文件名称","fileName"),
    FILE_CONTENT("文件内容","fileContent");


    private final String text;

    private final String value;

    // 使用 HashMap 存储 value 到 Enum 的映射关系
    private static final Map<String, FileFilterRangeEnum> VALUE_MAP = new HashMap<>();

    // 静态代码块，在类加载时初始化 VALUE_MAP
    static {
        for (FileFilterRangeEnum enumValue : FileFilterRangeEnum.values()) {
            VALUE_MAP.put(enumValue.value, enumValue);
        }
    }

    FileFilterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static FileFilterRangeEnum getByValue(String value) {
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        if (VALUE_MAP.containsKey(value)) {
            return VALUE_MAP.get(value);
        }
        return null;
    }

}
