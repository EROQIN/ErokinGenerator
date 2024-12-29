package com.erokin.maker.generator;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.erokin.maker.generator.file.DynamicGenerator;
import com.erokin.maker.mata.Meta;
import com.erokin.maker.mata.MetaManager;

import java.io.File;

public class MainGenerator {
    public static void main(String[] args) {
        Meta meta = MetaManager.getMetaObject();  //初始化meta对象
        System.out.println(meta.getModelConfig());
        String projectPath = System.getProperty("user.dir");   //获取项目路径
        String outputPath = projectPath + File.separator + "generated";   //输出路径
        // 读取resources目录：
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourePath = classPathResource.getAbsolutePath();

        //Java包中的基础路径
        String outPutBasePackage = meta.getBasePackage();
        //com.erokin=>com\erokin
        String outPutpackagePath = StrUtil.join(File.separator,StrUtil.split(outPutBasePackage,"."));
        String outPutBaseJavaPackagePath = outputPath + File.separator +"src\\main\\java"+File.separator+ outPutpackagePath;
        System.out.println(outPutBaseJavaPackagePath);

        String inputFilePath;
        String outPutFilePath;
        //model.DataModel.java 
        inputFilePath = inputResourePath + File.separator+ "templates\\java\\model\\DataModel.java.ftl";
        outPutFilePath = outPutBaseJavaPackagePath + File.separator + "model" + File.separator + "DataModel.java";
        DynamicGenerator.doGenerate(inputFilePath,outPutFilePath,meta);

    }
}
