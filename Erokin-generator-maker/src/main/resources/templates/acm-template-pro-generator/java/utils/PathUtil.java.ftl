package ${basePackage}.utils;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 工具类：专门用于获取路径
 */
public class PathUtil {

    private PathUtil() {};

    /**
     * 获取与JAR包平级的名为outOfJarFile的路径
     * @param outJarFileName 需要获取的在jar包外的文件名
     * @return 指定文件的路径字符串
     * @author Erokin
     */
    public static String getOutJarFolderPath(String outJarFileName) {
        try {
            // 获取当前类的Class对象
            Class<?> clazz = cn.hutool.core.io.file.PathUtil.class;

            // 如果你的类是在JAR包内，则使用以下方式获取URL
            URL url = clazz.getProtectionDomain().getCodeSource().getLocation();

            // 将URL转换为URI，然后解析成File对象来获取父目录
            Path jarDirPath = Paths.get(url.toURI()).getParent();

            // 构建output文件夹的路径
            Path outputPath = jarDirPath.resolve(outJarFileName);

            // 返回output文件夹的路径字符串
            return outputPath.toString();
        } catch (URISyntaxException e) {
            // 处理可能的异常
            e.printStackTrace();
            return null; // 或者抛出自定义异常，根据你的需求决定
        }
    }

    public static void main(String[] args) {
        // 测试调用该方法
        String outputPath = getOutJarFolderPath("output");
        if (outputPath != null) {
            System.out.println("Output folder path: " + outputPath);
        } else {
            System.err.println("Failed to determine the output folder path.");
        }
    }
}