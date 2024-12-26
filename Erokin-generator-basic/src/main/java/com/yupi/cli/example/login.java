package com.yupi.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.Arrays;
import java.util.concurrent.Callable;

@Command(name = "login", description = "Login to the system",mixinStandardHelpOptions = true)
public class login implements Callable<Integer> {

    @Option(names = {"-u", "--username"},arity = "0..1", description = "Username for login")
    private String username;
    //设置交互式输入,类似scanf的输入方式（如果你使用jar包运行，echo的为false（默认）的输入不会显示在界面中）,arity指定能接收几个参数
    @Option(names = {"-p", "--password"}, description = "Password for login" ,arity = "0..1",interactive = true,echo = false,prompt = "请输入密码")
    private String password;
    //如果有多个带有interactive参数的属性，需要补充-cp参数
    @CommandLine.Option(names = {"-cp", "--confirmPassword"},arity = "0..1",interactive = true, description = "Confirm Password for login" ,echo = false,prompt = "请再次输入密码")
    private String confirmPassword;
    @Override
    public Integer call() throws Exception {
        System.out.println("Password : "+password);
        System.out.println("Confirm Password : "+confirmPassword);

        return 0;//退出码
    }
    public static void main(String[] args) {
        args = new String[] {"-u","1234"};
        //检验对应参数是否在args中
        if(!Arrays.asList(args).contains("-p")){
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "-p";
        }
        //检验-cp参数
        if(!Arrays.asList(args).contains("-cp")){
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = "-cp";
        }
        int exitCode = new CommandLine(new login()).execute(args);
        System.exit(exitCode);
    }
}
