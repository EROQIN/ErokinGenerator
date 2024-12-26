package com.yupi.cli;

import com.yupi.cli.command.ConfigCommand;
import com.yupi.cli.command.GenerateCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "CommandExecutor", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {
    private CommandLine commandLine;
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new GenerateCommand());
    }

    @Override
    public void run() {
        System.out.println("请输入具体参数");
    }
    public Integer doExecute(String[] args){
        return commandLine.execute(args);
    }
}
