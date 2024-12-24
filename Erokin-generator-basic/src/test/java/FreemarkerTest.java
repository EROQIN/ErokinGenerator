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
        // Create your Configuration instance, and specify if up to what FreeMarker
// version (here 2.3.22) do you want to apply the fixes that are not 100%
// backward-compatible. See the Configuration JavaDoc for details.
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setNumberFormat("0.######");  // now it will print 1000000

// where cfg is a freemarker.template.Configuration object
// Specify the source where the template files come from. Here I set a
// plain directory for it, but non-file-system sources are possible too:
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources/template"));

// Set the preferred charset template files are stored in. UTF-8 is
// a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

// Sets how errors will appear.
// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        Template template = cfg.getTemplate("test.txt.ftl");
        //指定输出流流向的文件

        Writer out = new FileWriter("test.txt");


        HashMap<String, Object> dataModel = new HashMap<>();

        dataModel.put("name", "World");
        dataModel.put("age", 180000);
        dataModel.put("author", "Kinzuki");

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2da");
        list.add("3");
        dataModel.put("menuItems",list);

        template.process(dataModel, out);

        out.close();

    }

}
