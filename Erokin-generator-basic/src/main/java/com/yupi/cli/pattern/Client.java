package com.yupi.cli.pattern;

public class Client {
    //客户端要做的是对着遥控器填充指令，点击发射指令
    public static void main(String[] args) {
        Device device1 = new Device("AAA");
        Device device2 = new Device("BBB");

        RemoteController remoteController = new RemoteController();
        remoteController.setCommand(new TurnOnCommand(device1));
        remoteController.buttonPressed();
        remoteController.setCommand(new TurnOffCommand(device2));
        remoteController.buttonPressed();
    }
}
