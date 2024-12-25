package com.yupi.cli.example;

import cn.hutool.db.sql.SqlBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
// some exports omitted for the sake of brevity

@Command(name = "ASCIIArt", version = "ASCIIArt 1.0", mixinStandardHelpOptions = true) 
public class ASCIIArt implements Runnable { 
    //通过命令行指令来接收值(指定输入什么用于传值）（description用于在help手册中展示）
    @Option(names = { "-s", "--font-size" }, description = "Font size",required = false)
    int fontSize = 19;


    @Parameters(paramLabel = "<word>", defaultValue = "Hello, picocli", 
               description = "Words to be translated into ASCII art.")
    private String[] words = { "Hello,", "picocli" };  //把选项设置为数组即可多值输入

    @Override
    public void run() {
        //自己实现业务逻辑
        System.out.println("fontSize:"+fontSize);
        System.out.println("words:"+ String.join(",",words));
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ASCIIArt()).execute(args); 
        System.exit(exitCode); 
    }
}