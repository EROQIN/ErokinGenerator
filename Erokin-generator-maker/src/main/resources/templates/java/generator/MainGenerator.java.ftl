package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;


<#macro generateFile indent fileInfo>
${indent}inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
<#if fileInfo.type == "static">
${indent}StaticGenerator.copyFilesByHutool(inputPath, outputPath);
<#else>
${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
</#if>
</#macro>


/**
 * 核心生成器
 */
public class MainGenerator {

    /**
     * 生成
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(DataModel model) throws TemplateException, IOException {
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        String inputPath;
        String outputPath;

<#list modelConfig.models as modelInfo>
        ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
</#list>


<#list fileConfig.files as fileInfo>
    <#if fileInfo.groupKey??>
    <#--  存在condition时，添加条件，再生成  -->
        <#if fileInfo.condition ??>
        if (${fileInfo.condition}){
            <#list fileInfo.files as fileInfo>
                <@generateFile fileInfo=fileInfo indent="            "/>

            </#list>
        }
        <#--  不存在condition，直接生成  -->
        <#else>
            <#list fileInfo.files as fileInfo>
                <@generateFile fileInfo=fileInfo indent="       "/>

            </#list>
        </#if>
    <#--  不存在groupKey时，判断是否存在condition  -->
    <#else>
        <#if fileInfo.condition ??>
            if (${fileInfo.condition}){
                <@generateFile fileInfo=fileInfo indent="       "/>

            }
        <#else>
            <@generateFile fileInfo=fileInfo indent="       "/>

        </#if>
    </#if>
</#list>
    }
}