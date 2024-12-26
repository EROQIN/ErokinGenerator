package com.yupi.cli.pattern;

public class RemoteController {
    //遥控器要做的，是记录命令，并且可被点击发射命令
    private Command command;
    public void setCommand(Command command) {
        this.command = command;
    }
    public void buttonPressed() {
        command.execute();
    }
}
