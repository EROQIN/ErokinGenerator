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
        //------------FreeMarker----------------
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setNumberFormat("0.######");  // now it will print 1000000
        // 模板文件所在的目录
        String templatePath = "Erokin-generator-basic"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"template";
        cfg.setDirectoryForTemplateLoading(new File(templatePath));

        //字符集设置
        cfg.setDefaultEncoding("UTF-8");
        //错误信息输出设置
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        //获取模板
        Template template = cfg.getTemplate("MainTemplate.java.ftl");
        //指定输出流流向的文件,创建流管道
        Writer out = new FileWriter("MainTemplate.java");
        //创建数据填充模型
        MainTemplateConfig dataModel = new MainTemplateConfig();
        dataModel.setAuthor("Kinzuki");
        dataModel.setOutputText("Hello World");
        dataModel.setLoop(true);
        //最终执行！
        template.process(dataModel, out);
        //善后工作
        out.close();

    }




    /**
     * 通过FreeMarker生成文件
     * @param templatePath 模板路径(相对于template目录下的)
     * @param outputPath 输出路径
     * @param dataModel 填充数据
     */
    public void GnerateFileByFreemarker(String templatePath, String outputPath,Object dataModel) throws IOException, TemplateException {
        //------------FreeMarker----------------
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setNumberFormat("0.######");  // now it will print 1000000
        // 文件所在的目录
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources/template"));
        //字符集设置
        cfg.setDefaultEncoding("UTF-8");
        //错误信息输出设置
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        //获取模板
        Template template = cfg.getTemplate("test.txt.ftl");
        //指定输出流流向的文件,创建流管道
        Writer out = new FileWriter("test.txt");
        //创建填充数据模型
//        HashMap<String, Object> dataModel = new HashMap<>();
//
//        dataModel.put("name", "World");
//        dataModel.put("age", 180000);
//        dataModel.put("author", "Kinzuki");
//
//        List<String> list = new ArrayList<>();
//        list.add("1");
//        list.add("2da");
//        list.add("3");
//        dataModel.put("menuItems",list);
        //模板+填充数据=>输出文件
        template.process(dataModel, out);
        //关闭流
        out.close();
    }
}
