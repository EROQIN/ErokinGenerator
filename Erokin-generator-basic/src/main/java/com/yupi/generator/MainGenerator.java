package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;

import java.io.File;

import static com.yupi.generator.StaticGenerator.copyFilesByRecursive;

public class MainGenerator {
    public static void main(String[] args) {
        //静态文件生成
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "Erokin-generator-demo-projects" + File.separator + "acm-template";
        copyFilesByRecursive(inputPath, projectPath);

        //动态文件生成
        //获取模板文件路径：
        String dynamicInputPath = "Erokin-generator-basic\\src\\main\\resources\\template\\MainTemplate.java.ftl";
        String dynamicOutputPath = "acm-template\\src\\com\\yupi\\acm\\MainTemplate.java";
        //创建数据填充模型
        MainTemplateConfig dataModel = new MainTemplateConfig();
        dataModel.setAuthor("Kinzuki");
        dataModel.setOutputText("输出的结果为：");
        dataModel.setLoop(true);

        DynamicGenerator.doGenerate(dynamicInputPath, dynamicOutputPath, dataModel);



    }



}
