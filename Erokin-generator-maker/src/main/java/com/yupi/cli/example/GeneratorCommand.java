package com.yupi.cli.example;

import com.yupi.generator.DynamicGenerator;
import com.yupi.model.MainTemplateConfig;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.io.File;
import static com.yupi.generator.StaticGenerator.copyFilesByRecursive;

/**
 * MainGenerator 用于通过命令行参数生成文件。
 */
@Command(name = "Generator", version = "Generator 1.0", mixinStandardHelpOptions = true,description = "Generates files based on a template with options to customize the output.")
public class GeneratorCommand implements Runnable {

    // 定义命令行选项，与原程序中需要设置的属性对应
    @Option(names = { "-l", "--loop" }, description = "Whether to loop (true/false)")
    private boolean loop = true; // 默认值为 true

    @Option(names = { "-a", "--author" }, description = "Author of the document")
    private String author = "Kinzuki"; // 默认作者

    @Option(names = { "-o", "--outputText" }, description = "Output text prefix")
    private String outputText = "输出的结果为："; // 默认输出文本前缀

    /**
     * 实现业务逻辑。
     */
    @Override
    public void run() {
        // 获取项目的根路径作为默认输出路径
        String outPutPath = System.getProperty("user.dir");
        // 模板文件夹路径
        String inputPath = outPutPath + File.separator + "Erokin-generator-basic" + File.separator +"src"+File.separator+"main"+File.separator+"resources"+File.separator+"template"+File.separator+"acm-template";
        copyFilesByRecursive(inputPath, outPutPath);

        // 创建数据填充模型，并用命令行参数初始化
        MainTemplateConfig dataModel = new MainTemplateConfig();
        dataModel.setAuthor(author);
        dataModel.setOutputText(outputText);
        dataModel.setLoop(loop);

        // 获取inputPath的最后一级目录名
        String templateName = inputPath.substring(inputPath.lastIndexOf(File.separator) + 1);
        ReplaceTemplateFile(outPutPath + File.separator + templateName, dataModel);
    }

    /**
     * 替换复制后的文件夹中的.ftl文件
     * @param path 含有.ftl文件的文件夹的路径
     * @param dataModel 填充的数据模型
     */
    public static void ReplaceTemplateFile(String path, Object dataModel){
        // 对文件夹进行遍历：
        File file = new File(path);
        if (file.isDirectory()){
            for (File f : file.listFiles()){
                if (f.isDirectory()){
                    ReplaceTemplateFile(f.getAbsolutePath(), dataModel);
                } else {
                    // 对文件进行替换：
                    // 判断文件名是否以.ftl结尾
                    if (f.getName().endsWith(".ftl")){
                        // 模板文件+数据模型
                        DynamicGenerator.doGenerate(f.getAbsolutePath(), f.getAbsolutePath().replace(".ftl", ""), dataModel);
                        // 删除模板文件
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 主方法，解析命令行参数并执行任务。
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new GeneratorCommand()).execute(args);
        System.exit(exitCode);
    }
}