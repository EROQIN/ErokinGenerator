package com.yupi;

import com.yupi.cli.CommandExecutor;

public class Main {
    public static void main(String[] args) {
        args = new String[] {"generate"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}