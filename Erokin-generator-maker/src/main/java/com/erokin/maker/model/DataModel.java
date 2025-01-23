package com.erokin.maker.model;

import lombok.Data;

@Data
public class DataModel {
    /**
     * 是否需要gitignore（开关）
     */
    public boolean needGit = true;
    /**
     * 作者（字符串，填充值）
     */
    public String author = "Erokin";
    /**
     * 输出信息（字符串，填充值）
     */
    public String outputText = "Hello World";
    /**
     * 是否循环（开关）
     */
    public boolean loop = false;

}
