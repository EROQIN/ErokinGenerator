package com.erokin.maker.template;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.erokin.maker.meta.Meta;
import com.erokin.maker.meta.enums.FileGenerateTypeEnum;
import com.erokin.maker.meta.enums.FileTypeEnum;
import com.erokin.maker.template.enums.FileFilterRangeEnum;
import com.erokin.maker.template.enums.FileFilterRuleEnum;
import com.erokin.maker.template.model.FileFilterConfig;
import com.erokin.maker.template.model.TemplateMakerFileConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
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
     * @param templateMakerFileConfig
     * @param modelInfo
     * @param searchStr
     * @param id
     * @return id
     */
    private static long makeTemplate(Meta newMeta,
                                     String originProjectPath,
                                     TemplateMakerFileConfig templateMakerFileConfig,
                                     Meta.ModelConfig.ModelInfo modelInfo,
                                     String searchStr,
                                     Long id

    ){

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
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();

        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String fileInputPath = fileInfoConfig.getPath();
            //如果填的是相对路径，要改为绝对路径
            if(!fileInputPath.startsWith(sourceRootPath)){
                fileInputPath = sourceRootPath + File.separator + fileInputPath;
            }
            //String inputFileAbsolutePath = sourceRootPath + File.separator + fileInputPath;


                List<File> fileList = FileFilter.doFilter(fileInputPath, fileInfoConfig.getFilterConfigList());
                for(File file : fileList){
                    Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath,file);
                    newFileInfoList.add(fileInfo);
                }

        }

        //endregion
        // 如果是文件组

        //region 三、生成元信息文件

        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            // 新增分组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            // 文件全放到一个分组内
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }

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



        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());


        //判断是否和原文件内容一致，如果一致,静态生成
        if (fileContent.equals(newFileContent)){
            //输出路径=输入路径
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        }else {
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
            //输出模板文件
            FileUtil.writeUtf8String(newFileContent, outputAbsolutePath);
        }



        return fileInfo;
    }

    /**
     * TODO:文件过滤
     */



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
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/common";
        String fileInputPath2 = "src/main/java/com/yupi/springbootinit/constant";
        List<String> fileInputPathList = new ArrayList<>();
        fileInputPathList.add(fileInputPath1);
        fileInputPathList.add(fileInputPath2);
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
        String searchStr = "BaseResponse";
        // 文件过滤
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(fileInputPath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);



        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2));



        // 分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);


        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig, modelInfo, searchStr, 1735281524670181376L);
        System.out.println(id);

    }


    /**
     * 文件去重
     */
    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 策略：同分组内文件 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey)
                );


        // 2. 同组内的文件配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        // 3. 将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4. 将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream().filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
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
