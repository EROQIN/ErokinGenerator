package com.erokin.maker.generator.file;

import cn.hutool.core.io.FileUtil;

public class StaticGenerator {
    /**
     * 复制文件
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }



}



