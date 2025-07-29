package com.erokin.maker.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 清理之前的构建并打包
        // 注意不同操作系统，执行的命令不同
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";
        String mavenCommand = otherMavenCommand; // 默认使用非Windows命令

        // 检查操作系统类型，Windows系统使用mvn.cmd，其他系统使用mvn
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            mavenCommand = winMavenCommand;
        }

        // 检查目录是否存在
        File dir = new File(projectDir);
        if (!dir.exists()) {
            throw new RuntimeException("项目目录不存在: " + projectDir);
        }
        
        // 检查pom.xml是否存在
        File pomFile = new File(dir, "pom.xml");
        if (!pomFile.exists()) {
            throw new RuntimeException("pom.xml文件不存在: " + pomFile.getAbsolutePath());
        }

        // 解析命令并添加JVM参数以减少警告
        List<String> commandList = new ArrayList<>();
        commandList.add("mvn");
        // 添加参数以减少Unsafe相关的警告
        commandList.add("-Dsun.misc.Unsafe.allow=true");
        commandList.add("clean");
        commandList.add("package");
        commandList.add("-DskipTests=true");

        // 这里一定要拆分！
        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        //System.out.println(environment);
        Process process = processBuilder.start();

        // 使用StringBuilder收集输出，确保不会丢失任何信息
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        // 创建线程读取标准输出
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (IOException e) {
                errorOutput.append("读取输出时出错: ").append(e.getMessage()).append("\n");
            }
        });

        // 创建线程读取错误输出
        Thread errorThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorOutput.append(line).append("\n");
                }
            } catch (IOException e) {
                errorOutput.append("读取错误输出时出错: ").append(e.getMessage()).append("\n");
            }
        });

        // 启动线程
        outputThread.start();
        errorThread.start();

        // 等待命令执行完成
        int exitCode = process.waitFor();
        
        // 等待读取线程完成
        try {
            outputThread.join();
            errorThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("等待输出读取完成时被中断", e);
        }

        // 只有在出现错误时才输出错误信息
        if (exitCode != 0 || errorOutput.length() > 0) {
            if (output.length() > 0) {
                System.out.println(output.toString());
            }
            
            if (errorOutput.length() > 0) {
                System.err.println(errorOutput.toString());
            }
        }

        if (exitCode != 0) {
            throw new RuntimeException("Maven构建失败，退出码：" + exitCode);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("/Users/erokin/develop/Erokin-generator/Erokin-generator-maker/generated/acm-template-pro-generator");
    }
}
