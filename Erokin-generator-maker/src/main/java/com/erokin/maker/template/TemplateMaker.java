package com.erokin.maker.template;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.erokin.maker.meta.Meta;
import com.erokin.maker.meta.enums.FileGenerateTypeEnum;
import com.erokin.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板制作工具
 */
public class TemplateMaker {
    /*
    TODO:输入所需操作文件的基本信息
    TODO:输入文件信息
    TODO:输入模板参数
    TODO:输出模板文件
    TODO:输出元信息文件
    */

    //main用于测试开发，产出方法
    public static void main(String[] args) throws Exception {
        //region 一、获取目标文件的基本信息：
        //1.项目的基本信息
        String name = "acm-template";
        String description = "ACM 示例模板生成器";
        //2.输入文件信息
        String projectPath = System.getProperty("user.dir");
        //要挖坑的根目录
        String sourceRootPath = new File(projectPath).getParent() + File.separator + "Erokin-generator-demo-projects/acm-template";
        //win系统下需要对文件路径进行替换
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
        String fileInputPath = "src/com/yupi/acm/MainTemplate.java";
        String fileOutputPath = fileInputPath + ".ftl";

        //3.输入模型参数信息
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("outputText");
        modelInfo.setType("String");
        modelInfo.setDefaultValue("sum：");

        //endregion
        //region 二、使用字符串替换，生成模板文件
        String fileAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileContent = FileUtil.readUtf8String(fileAbsolutePath);
        //替换字符串,置换物
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, "Sum：",replacement);
        //输出模板文件
        //获取文件的输出完整路径
        String outputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        FileUtil.writeUtf8String(newFileContent, outputAbsolutePath);
        //endregion
        //region 三、生成元信息文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        //1. 构造配置参数对象
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);

        Meta.FileConfig fileConfig = new Meta.FileConfig();
        fileConfig.setSourceRootPath(sourceRootPath);
        meta.setFileConfig(fileConfig);

        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        //list内的fileInfo
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        //存入list
        fileInfoList.add(fileInfo);
        //将list存入Files
        fileConfig.setFiles(fileInfoList);


        Meta.ModelConfig modelConfig = new Meta.ModelConfig();
        List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
        modelInfoList.add(modelInfo);
        modelConfig.setModels(modelInfoList);
        meta.setModelConfig(modelConfig);

        //2. 输出元信息文件（json）
        String jsonContent = JSONUtil.toJsonPrettyStr(meta);
        FileUtil.writeUtf8String(jsonContent, metaOutputPath);

        //endregion

    }





}
