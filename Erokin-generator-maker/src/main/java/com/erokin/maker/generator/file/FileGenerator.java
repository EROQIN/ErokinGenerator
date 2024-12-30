package com.erokin.maker.generator.file;

import java.io.File;

public class FileGenerator {

    /**
     * 替换复制后的文件夹中的.ftl文件
     * @param Path 含有.ftl文件的文件夹的路径
     * @param dataModel 填充的数据模型
     */
    public static void ReplaceTemplateFile(String Path, Object dataModel){
        //对文件夹进行遍历：
        File file = new File(Path);
        if (file.isDirectory()){
            for (File f : file.listFiles()){
                if (f.isDirectory()){
                    ReplaceTemplateFile(f.getAbsolutePath(), dataModel);
                }else {
                    //对文件进行替换：
                    //判断文件名是否以.ftl结尾

                    if (f.getName().endsWith(".ftl")){
                        //模板文件+数据模型
                        DynamicGenerator.dynamicGenerate(f.getAbsolutePath(), f.getAbsolutePath().replace(".ftl", ""),dataModel);
                        //删除模板文件
                        f.delete();
                    }

                }
            }
        }


    }



}
