package com.erokin.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.erokin.maker.template.enums.FileFilterRangeEnum;
import com.erokin.maker.template.enums.FileFilterRuleEnum;
import com.erokin.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FileFilter{

    /**
     * 多文件过滤
     * @param path 文件/文件夹路径
     * @param fileFilterConfigList 文件过滤规则集合
     * @return 过滤后的文件集合
     * @author erokin
     */
    public static List<File> doFilter(String path, List<FileFilterConfig> fileFilterConfigList){
        List<File> fileList = FileUtil.loopFiles(path);
        return fileList.stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());
    }


    /**
     * 单个文件的过滤
     * @param fileFilterConfigList
     * @param file
     * @return 该文件是否保留
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file){

        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);
        //过滤器校验的结果
        boolean result = true;

        if(CollUtil.isEmpty(fileFilterConfigList)){
            return true;
        }

        for(FileFilterConfig fileFilterConfig : fileFilterConfigList){
            //获取range,rule,value字段
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            //1.文件的过滤范围判断
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getByValue(range);
            if(fileFilterRangeEnum == null){
                continue;
            }
            //要过滤的内容(默认值设为fileName)
            String content = fileName;
            switch (fileFilterRangeEnum){
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            //根据规则对内容进行过滤
            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if(fileFilterRuleEnum == null){
                continue;
            }
            switch (fileFilterRuleEnum){
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
            //有一个不满足就直接返回
            if(!result){
                return false;
            }


        }
        //都满足：
        return true;
    }
}
