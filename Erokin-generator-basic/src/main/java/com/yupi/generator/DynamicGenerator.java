package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;
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
    public static void main(String[] args) throws IOException, TemplateException {
        //获取模板文件路径：
        String inputPath = "Erokin-generator-basic\\src\\main\\resources\\template\\MainTemplate.java.ftl";
        String outputPath = "Erokin-generator-basic\\src\\main\\resources\\MainTemplate.java";
        //创建数据填充模型
        MainTemplateConfig dataModel = new MainTemplateConfig();
        dataModel.setAuthor("Kinzuki");
        dataModel.setOutputText("Hello World");
        dataModel.setLoop(true);

        doGenerate(inputPath, outputPath, dataModel);
    }




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
