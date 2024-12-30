package ${basePackage}.maker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.maker.generator.file.FileGenerator;
import ${basePackage}.maker.generator.file.StaticGenerator;
import ${basePackage}.maker.model.DataModel;
import ${basePackage}.maker.utils.PathUtil;
import lombok.Data;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;
@Data
@CommandLine.Command(name = "generator", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {
     <#list modelConfig.models as modelInfo>
    <#if modelInfo.description??>
    /**
     * ${modelInfo.description}
     */
     </#if>
    @CommandLine.Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}"</#if>,<#if modelInfo.fieldName??>"--${modelInfo.fieldName}"</#if>}, <#if modelInfo.description??>description = "${modelInfo.description}",</#if> arity = "0..1",interactive = true,echo = true)
    private ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c} </#if>;

    </#list>

    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        //String projectPath = System.getProperty("user.dir");
        String outputPath = PathUtil.getOutJarFolderPath("output");
        String inputPath = PathUtil.getOutJarFolderPath("templates");
        StaticGenerator.copyFilesByHutool(inputPath+File.separator + "acm-template",outputPath);

        FileGenerator.ReplaceTemplateFile(outputPath+File.separator+"acm-template", dataModel);
        return 0;
    }
}
