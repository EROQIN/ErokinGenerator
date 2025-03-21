package com.erokin.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.erokin.maker.generator.JarGenerator;
import com.erokin.maker.generator.ScriptGenerator;
import com.erokin.maker.generator.file.DynamicGenerator;
import com.erokin.maker.generator.file.StaticGenerator;
import com.erokin.maker.meta.Meta;
import com.erokin.maker.meta.MetaManager;
import com.erokin.maker.meta.MetaValidator;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public abstract class GenerateTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();
        // 校验并填充数据
        MetaValidator.doValidAndFill(meta);

        // 输出根路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }
        //复制原始文件
        copySources(outputPath, meta);
        // 生成代码
        generateCode(meta, outputPath);
        // 构建 jar 包
        String jarPath = buildJar(outputPath, meta);
        // 构建脚本
        String shellOutputFilePath = buildScript(outputPath, jarPath);
        // 构建简化版程序
        buildDist(outputPath, jarPath, shellOutputFilePath);

    }
    protected String buildScript(String outputPath,String jarPath) throws IOException {
        // 封装脚本
        String shellOutputFilePath = outputPath + File.separator + "generator";

        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return  shellOutputFilePath;
    }



    protected void buildDist(String outputPath, String jarPath, String shellOutputFilePath) {
        //生成精简版程序
        String distOutputPath = outputPath + "-dist";
        //1.拷贝jar包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);

        //2.拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        FileUtil.copy(shellOutputFilePath +".bat", distOutputPath, true);

        //3.拷贝源模板文件
        FileUtil.copy(outputPath + File.separator + ".source", distOutputPath, true);
    }

    protected String buildJar(String outputPath,Meta meta) throws IOException, InterruptedException {
        // 构建 jar 包
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    protected void generateCode(Meta meta, String outputPath) {
        // 读取 resources 目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        // Java 包基础路径
        String outputBasePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        // model.DataModel
        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // generator.MainGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);


        // cli.command.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // cli.command.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // cli.command.ListCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // cli.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // Main
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // generator.DynamicGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicGenerator.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // generator.MainGenerator
        //inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        //outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        //DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // generator.StaticGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticGenerator.java";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // pom.xml
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

        // README.md
        inputFilePath = inputResourcePath + File.separator + "templates/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicGenerator.doGenerate(inputFilePath , outputFilePath, meta);

    }

    protected void copySources(String outputPath, Meta meta) {
        //将模板复制到.source目录下
        String outputSourcePath = outputPath + File.separator + ".source";
        String inputSourcePath = meta.getFileConfig().getSourceRootPath();
        StaticGenerator.copyFilesByHutool(inputSourcePath, outputSourcePath);
    }
}
