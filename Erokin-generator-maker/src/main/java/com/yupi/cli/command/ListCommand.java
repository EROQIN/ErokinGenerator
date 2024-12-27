package com.yupi.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

@CommandLine.Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {
    public void run() {
        List<File> files = FileUtil.loopFiles("D:\\develop\\Erokin-generator\\Erokin-generator-maker\\src\\main\\resources\\template\\acm-template");
        for (File file : files) {
            System.out.println(file.getAbsolutePath());
        }
    }
}
