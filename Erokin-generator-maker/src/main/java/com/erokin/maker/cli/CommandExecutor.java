package com.erokin.maker.cli;

import com.erokin.maker.cli.command.ConfigCommand;
import com.erokin.maker.cli.command.GenerateCommand;
import com.erokin.maker.cli.command.ListCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "CommandExecutor", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable {
    private CommandLine commandLine;
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ListCommand())
        ;
    }

    @Override
    public void run() {
        System.out.println("请输入具体参数");
    }
    public Integer doExecute(String[] args){
        return commandLine.execute(args);
    }
}
