package com.erokin.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.HashMap;

/**
 * 文件过滤规则枚举
 */
@Getter
public enum FileFilterRuleEnum {

    CONTAINS("包含", "contains"),
    STARTS_WITH("前缀匹配", "startsWith"),
    ENDS_WITH("后缀匹配", "endsWith"),
    REGEX("正则", "regex"),
    EQUALS("相等", "equals");

    private final String text;

    private final String value;

    private static final HashMap<String,FileFilterRuleEnum> VALUE_MAP = new HashMap<>();

    static {
        for (FileFilterRuleEnum anEnum : FileFilterRuleEnum.values()) {
            VALUE_MAP.put(anEnum.value, anEnum);
        }
    }

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static FileFilterRuleEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        if (VALUE_MAP.containsKey(value)) {
            return VALUE_MAP.get(value);
        }
        return null;
    }
}
