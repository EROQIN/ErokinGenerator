package com.erokin.maker.generator.file;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * 动态文件生成器
 */
public class DynamicGenerator {

    /**
     * 通过FreeMarker生成文件
     * @param inputPath 模板路径
     * @param outputPath 输出路径
     * @param dataModel 填充数据
     */
    public static void doGenerate(String inputPath, String outputPath, Object dataModel) {
        //------------FreeMarker----------------
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setNumberFormat("0.######");  // now it will print 1000000
        // 模板文件所在的目录
        File templateDir = new File(inputPath).getParentFile();

        try {
            cfg.setDirectoryForTemplateLoading(templateDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //字符集设置
        cfg.setDefaultEncoding("UTF-8");
        //错误信息输出设置
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            String templateName = new File(inputPath).getName();
            //获取模板
            Template template = cfg.getTemplate(templateName);
            // 创建输出文件对象
            File outputFile = new File(outputPath);

            // 确保输出文件的父目录存在
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();  // 创建所有必要的父目录
            }
            //指定输出流流向的文件,创建流管道
            Writer out = new FileWriter(outputPath);

            //最终执行！
            template.process(dataModel, out);
            //善后工作
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
