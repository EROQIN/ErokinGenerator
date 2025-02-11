package com.erokin.maker.template;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.erokin.maker.meta.Meta;
import com.erokin.maker.meta.enums.FileGenerateTypeEnum;
import com.erokin.maker.meta.enums.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板制作工具
 */
@Slf4j
public class TemplateMaker {

    /*
    TODO:让程序具有状态，支持追加模板参数
    */

    /**
     * 制作模板
     * @param newMeta
     * @param originProjectPath
     * @param fileInputPath
     * @param modelInfo
     * @param searchStr
     * @param id
     * @return id
     */
    private static long makeTemplate(Meta newMeta, String originProjectPath,String fileInputPath, Meta.ModelConfig.ModelInfo modelInfo,String searchStr, Long id){

        //没有id则生成id
        if(id == null){
            id = IdUtil.getSnowflakeNextId();
        }
        //输入信息
        //要挖坑的根目录
       //复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if(!FileUtil.exist(templatePath)){
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

        //region 一、获取目标文件的基本信息：
        //2.输入文件信息
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        //win系统下需要对文件路径进行替换
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");


        String inputFileAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        if (FileUtil.isDirectory(inputFileAbsolutePath)) {
            List<File> fileList = FileUtil.loopFiles(inputFileAbsolutePath);
            for(File file : fileList){
            Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath,file);
            newFileInfoList.add(fileInfo);
            }
        }else {
            Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath,new File(inputFileAbsolutePath));
            newFileInfoList.add(fileInfo);
        }

        //endregion
        //region 三、生成元信息文件


        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        //已有meta文件，在原有基础上进行修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            //1.追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = oldMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);

            List<Meta.ModelConfig.ModelInfo> modelInfoList = oldMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);

            //配置去重
            oldMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            oldMeta.getModelConfig().setModels(distinctModels(modelInfoList));
            //2.生成元信息文件
            String jsonContent = JSONUtil.toJsonPrettyStr(oldMeta);



            //输出元信息文件
            FileUtil.writeUtf8String(jsonContent, metaOutputPath);
        }
        //无元信息文件，创建元信息文件并初始化
        else {

            //1. 构造配置参数对象


            Meta.FileConfig fileConfig = new Meta.FileConfig();
            fileConfig.setSourceRootPath(sourceRootPath);
            newMeta.setFileConfig(fileConfig);

            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            //list内的fileInfo

            //存入list
            fileInfoList.addAll(newFileInfoList);;

            //将list存入Files
            fileConfig.setFiles(fileInfoList);


            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelInfoList.add(modelInfo);
            modelConfig.setModels(modelInfoList);
            newMeta.setModelConfig(modelConfig);

            //2. 输出元信息文件（json）
            String jsonContent = JSONUtil.toJsonPrettyStr(newMeta);
            FileUtil.writeUtf8String(jsonContent, metaOutputPath);
        }
        //endregion




        return id;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(Meta.ModelConfig.ModelInfo modelInfo, String searchStr, String sourceRootPath, File inputFile) {

        String inputFileAbsolutePath = inputFile.getAbsolutePath();
        inputFileAbsolutePath = inputFileAbsolutePath.replaceAll("\\\\", "/");
        String fileInputPath =inputFileAbsolutePath.replace(sourceRootPath+"/", "");
        String fileOutputPath = fileInputPath + ".ftl";


        //endregion
        //region 二、使用字符串替换，生成模板文件
        String fileAbsolutePath = inputFile.getAbsolutePath();
        String outputAbsolutePath = inputFile.getAbsolutePath() + ".ftl";

        String fileContent;
        //如果已有模板文件，表示不是第一次制作，则在原有基础上继续挖坑
        if(FileUtil.exist(outputAbsolutePath)){
            fileContent = FileUtil.readUtf8String(outputAbsolutePath);
        } else{
            fileContent = FileUtil.readUtf8String(fileAbsolutePath);
        }


        //替换字符串,置换物
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, searchStr,replacement);
        //输出模板文件
        //获取文件的输出完整路径

        FileUtil.writeUtf8String(newFileContent, outputAbsolutePath);

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        return fileInfo;
    }


    //main用于测试开发，产出方法
    public static void main(String[] args) throws Exception {
        log.info("开始制作模板...");

        Meta meta = new Meta();
        meta.setName("acm-template");
        meta.setDescription("ACM 示例模板生成器");
        //要挖坑的根目录
        String projectPath = System.getProperty("user.dir");
        //指定原始项目路径，
        String originProjectPath = new File(projectPath).getParent() + File.separator + "Erokin-generator-demo-projects/springboot-init";
        String fileInputPath = "src/main/java/com/yupi/springbootinit";
        //3.输入模型参数信息
        //Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        //modelInfo.setFieldName("outputText");
        //modelInfo.setType("String");
        //modelInfo.setDefaultValue("sum = ");


        //输入模板信息（第二次）
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        //替换变量（首次）
        //String searchStr = "Sum：";
        //第二次
        String searchStr = "MainTemplate";


        long id = makeTemplate(meta, originProjectPath, fileInputPath, modelInfo, searchStr, null);
        log.info("模板制作成功，id:{}",id);

    }


    /**
     * 文件去重
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList){
        ArrayList<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(fileInfoList.stream()
                .collect(
                        Collectors.toMap(com.erokin.maker.meta.Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                ).values());
        return newFileInfoList;

    }


    /**
     * 模型去重
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList){
        ArrayList<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(modelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values());

        return newModelInfoList;
    }




}
