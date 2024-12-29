package ${basePackage}.maker.cli;

import ${basePackage}.maker.cli.command.ConfigCommand;
import ${basePackage}.maker.cli.command.GenerateCommand;
import ${basePackage}.maker.cli.command.ListCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "${name}", mixinStandardHelpOptions = true)
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
