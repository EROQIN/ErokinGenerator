package com.erokin.maker.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.erokin.maker.generator.file.DynamicGenerator;
import com.erokin.maker.generator.file.FileGenerator;
import com.erokin.maker.generator.file.StaticGenerator;
import com.erokin.maker.mata.Meta;
import com.erokin.maker.mata.MetaManager;

import java.io.File;

public class MainGenerator {
    public static void main(String[] args) {
        Meta meta = MetaManager.getMetaObject();  //初始化meta对象
        //System.out.println(meta.getModelConfig());
        String projectPath = System.getProperty("user.dir");   //获取项目路径
        String outputBasePath = projectPath + File.separator + meta.getFileConfig().getOutputRootPath();
        String inputBasePath = projectPath + File.separator + meta.getFileConfig().getInputRootPath();
        //遍历文件列表, 根据文件属性生成文件
        for (Meta.FileConfig.FileInfo fileInfo : meta.getFileConfig().getFiles()) {
            String inputPath = inputBasePath + File.separator + fileInfo.getInputPath();
            String outputPath = outputBasePath + File.separator + fileInfo.getOutputPath();
            System.out.println(inputPath + "\n==>\n" + outputPath);
            if (fileInfo.getType().equals("dynamic")) {
                DynamicGenerator.dynamicGenerate(inputPath, outputPath, meta);
            } else {
                FileUtil.copyFile(inputPath, outputPath);
            }
        }


        //输出路径
        // 读取resources目录：
        //ClassPathResource classPathResource = new ClassPathResource("");
        //String inputResourePath = classPathResource.getAbsolutePath();

        //Java包中的基础路径
        //String outPutBasePackage = meta.getBasePackage();
        //com.erokin=>com\erokin
        //String outPutpackagePath = StrUtil.join(File.separator,StrUtil.split(outPutBasePackage,"."));


        //DynamicGenerator.dynamicGenerate(inputFilePath,outPutFilePath,meta);
    }
}

