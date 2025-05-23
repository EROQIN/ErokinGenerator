# ${name}
> ${description}
> 
> 作者：${author}

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 项目说明

执行项目根路径下的脚本文件：

```cmd
generator <命令> <选项参数>
```

示例指令：
```cmd
generator generate <#list modelConfig.models as modelInfo><#if modelInfo.abbr??> -${modelInfo.abbr} </#if></#list>
```

## 参数说明：

<#list modelConfig.models as modelInfo>
<#if modelInfo.fieldName??>${modelInfo?index+1}) ${modelInfo.fieldName}</#if>

类型：${modelInfo.type}

描述：${modelInfo.description}

<#if  modelInfo.defaultValue??>默认值：${modelInfo.defaultValue?c}</#if>

<#if modelInfo.abbr??>缩写：-${modelInfo.abbr}</#if>

</#list>