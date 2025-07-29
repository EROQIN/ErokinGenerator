package com.erokin.maker.generator.main;

public class MainGenerator extends GenerateTemplate {

    //不用生成精简版
    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        System.out.println("不要给我输出 dist 啦！");
        return "";
    }
    // public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
    //     MainGenerator mainGenerator = new MainGenerator();
    //     mainGenerator.doGenerate();
    //     System.out.println("生成完毕");
    // }
}