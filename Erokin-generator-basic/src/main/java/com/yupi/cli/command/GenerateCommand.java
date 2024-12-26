package com.yupi.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.yupi.generator.DynamicGenerator;
import com.yupi.generator.MainGenerator;
import com.yupi.generator.StaticGenerator;
import com.yupi.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;
@Data
@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    /**
     * 作者（字符串，填充值）
     */
    @CommandLine.Option(names = {"-a", "--author"}, description = "作者", arity = "0..1",interactive = true)
    private String author = "Erokin";
    /**
     * 输出信息（字符串，填充值）
     */
    @CommandLine.Option(names = {"-o", "--output"}, description = "输出信息", arity = "0..1",interactive = true)
    private String outputText = "Hello World";
    /**
     * 是否循环（开关）
     */
    @CommandLine.Option(names = {"-l", "--loop"}, description = "是否循环", arity = "0..1",interactive = true)
    private boolean loop = false;

    @Override
    public Integer call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        String projectPath = System.getProperty("user.dir");
        StaticGenerator.copyFilesByRecursive(projectPath+File.separator + "Erokin-generator-basic" +File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"template"+File.separator + "acm-template", projectPath+File.separator+"output");

        MainGenerator.ReplaceTemplateFile(projectPath+File.separator+"output"+File.separator+"acm-template", mainTemplateConfig);
        return 0;
    }
}
