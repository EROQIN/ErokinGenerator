package ${basePackage}.maker.model;

import lombok.Data;
/**
 * 模板数据模型
 */
@Data
public class DataModel {

    <#list modelConfig.models as ModelInfo>
        <#if ModelInfo.description??>
    /**
     * ${ModelInfo.description}
     */
        </#if>
    private ${ModelInfo.type} ${ModelInfo.fieldName} <#if ModelInfo.defaultValue??>
        = ${ModelInfo.defaultValue?c} </#if>;

    </#list>

}
