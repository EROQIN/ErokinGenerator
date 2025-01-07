package com.erokin.maker.meta;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.erokin.maker.meta.Meta.FileConfig;
import com.erokin.maker.meta.Meta.ModelConfig;
import com.erokin.maker.meta.enums.FileGenerateTypeEnum;
import com.erokin.maker.meta.enums.FileTypeEnum;
import com.erokin.maker.meta.enums.ModelTypeEnum;

import java.nio.file.Paths;
import java.util.List;

public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        //基础信息校验和设置默认值
        validAndFillMetaRoot(meta);
        //给fileConfig校验和默认值
        validAndFillFileConfig(meta);
        //给modelConfig校验和默认值
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        //modelConfig 校验和默认
        ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (CollUtil.isEmpty(models)) {
            return;
        }
        for (ModelConfig.ModelInfo model : models) {
            String fieldName = model.getFieldName();
            if (StrUtil.isBlankIfStr(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }
            String modelType = model.getType();
            if (StrUtil.isBlankIfStr(modelType)) {
                modelType = ModelTypeEnum.STRING.getValue();
                model.setType(modelType);
            }
            String modelDescription = model.getDescription();
            if (StrUtil.isBlankIfStr(modelDescription)) {
                modelDescription = "暂无";
                model.setDescription(modelDescription);
            }

        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        //FileConfig 校验和默认值
        FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        //必填项，如果为空，抛异常
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlankIfStr(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }
        //inputRootPath:.source + sourceRootPath的最后一个层级路径

        String inputRootPath = fileConfig.getInputRootPath();
        if (StrUtil.isEmpty(inputRootPath)) {
            inputRootPath = ".source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).toString();
            fileConfig.setInputRootPath(inputRootPath);
        }
        //outputRootPath:默认为项目路径下的generated
        String outputRootPath = fileConfig.getOutputRootPath();
        if (StrUtil.isEmpty(outputRootPath)) {
            outputRootPath = "generated";
            fileConfig.setOutputRootPath(outputRootPath);
        }
        String type = fileConfig.getType();
        if (StrUtil.isEmpty(type)) {
            type = FileTypeEnum.DIR.getValue();
            fileConfig.setType(type);
        }
        List<FileConfig.FileInfo> files = fileConfig.getFiles();
        if (CollUtil.isEmpty(files)) {
            return;
        }
        for (FileConfig.FileInfo file : files) {
            String inputPath = file.getInputPath();
            if (StrUtil.isBlankIfStr(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            String outputPath = file.getOutputPath();
            //默认为inputPathif
            if (StrUtil.isBlankIfStr(outputPath)) {
                outputPath = inputPath;
                file.setOutputPath(outputPath);
            }
            //默认为文件的末尾的类型
            String fileType = file.getType();
            if (StrUtil.isBlankIfStr(fileType)) {
                if (StrUtil.isEmpty(FileUtil.getSuffix(inputPath))) {
                    fileType = FileTypeEnum.DIR.getValue();
                } else {
                    file.setType(FileTypeEnum.FILE.getValue());
                }
            }
            //文件末尾不为.ftl时，为static，否则是dynamic
            String generateType = file.getGenerateType();
            if (!StrUtil.isBlankIfStr(generateType)) {
                continue;
            }
            if (inputPath.endsWith(".ftl")) {
                generateType = FileGenerateTypeEnum.DYNAMIC.getValue();
            } else {
                generateType = FileGenerateTypeEnum.STATIC.getValue();
            }
            file.setGenerateType(generateType);
        }


    }

    /**
     * 验证meta的基础数据和设置默认值
     */
    private static void validAndFillMetaRoot(Meta meta) {
        //基础信息校验默认值
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        meta.setName(name);

        String description = StrUtil.blankToDefault(meta.getDescription(), "我的模板代码生成器");
        meta.setDescription(description);

        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.erokin.generator");
        meta.setBasePackage(basePackage);

        String version = StrUtil.blankToDefault(meta.getVersion(), "1.0.0");
        meta.setVersion(version);

        String author = StrUtil.blankToDefault(meta.getAuthor(), "erokin");
        meta.setAuthor(author);

        String createTime = StrUtil.blankToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setCreateTime(createTime);
    }
}
