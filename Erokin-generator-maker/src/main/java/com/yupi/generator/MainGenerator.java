package com.yupi.generator;

import com.yupi.model.MainTemplateConfig;
import picocli.CommandLine;

import java.io.File;

import static com.yupi.generator.StaticGenerator.copyFilesByRecursive;

public class MainGenerator {
    public static void main(String[] args) {
        //如何提高现有代码的易用性：
        //1.根据模板名称在新生成的文件夹中寻找应替换的文件
        //静态文件生成
        String outPutPath = System.getProperty("user.dir");  //获取项目的根路径
        String inputPath = outPutPath + File.separator + "Erokin-generator-maker" + File.separator +"src"+File.separator+"main"+File.separator+"resources"+File.separator+"template"+File.separator+"acm-template";
        copyFilesByRecursive(inputPath, outPutPath);
        //创建数据填充模型
        MainTemplateConfig dataModel = new MainTemplateConfig();
        dataModel.setAuthor("Kinzuki");
        dataModel.setOutputText("输出的结");
        dataModel.setLoop(true);
        //获取inputPath的最后一级目录名
        String templateName = inputPath.substring(inputPath.lastIndexOf(File.separator) + 1);
        ReplaceTemplateFile(outPutPath + File.separator + templateName, dataModel);

    }

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
                        DynamicGenerator.doGenerate(f.getAbsolutePath(), f.getAbsolutePath().replace(".ftl", ""),dataModel);
                        //删除模板文件
                        f.delete();
                    }

                }
            }
        }


    }



}
