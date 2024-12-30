package ${basePackage}.maker.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

@CommandLine.Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    public void run() {
        //输入路径
        String inputPath = "${fileConfig.intputRootPath}"
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}