package com.yupi.cli.pattern;
//一个命令的要素，命令下达给谁?什么样的命令？发射命令？
public class TurnOnCommand implements Command{
    private Device device;

    public TurnOnCommand(Device device) {
        this.device = device;
    }
    @Override
    public void execute() {
        device.turnOn();
    }
}
