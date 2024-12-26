package com.yupi.cli.pattern;

public class Device {
    private String name;
    public Device(String name) {
        this.name = name;
    }
    public void turnOn() {
        System.out.println("Turning on " + name);
    }
    public void turnOff() {
        System.out.println("Turning off " + name);
    }
}
