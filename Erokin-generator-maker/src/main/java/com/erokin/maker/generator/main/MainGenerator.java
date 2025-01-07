package com.erokin.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

public class MainGenerator extends GenerateTemplate {

    //不用生成精简版
    @Override
    protected void buildDist(String outputPath, String jarPath, String shellOutputFilePath) {
        System.out.println("不生成dist");
    }

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}