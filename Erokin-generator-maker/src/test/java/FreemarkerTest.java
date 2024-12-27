import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FreemarkerTest {
    @Test
    public void test() throws IOException, TemplateException {
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
        HashMap<String, Object> dataModel = new HashMap<>();

        dataModel.put("name", "World");
        dataModel.put("age", 180000);
        dataModel.put("author", "Kinzuki");

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2da");
        list.add("3");
        dataModel.put("menuItems",list);
        //模板+填充数据=>输出文件
        template.process(dataModel, out);
        //关闭流
        out.close();
    }

}
