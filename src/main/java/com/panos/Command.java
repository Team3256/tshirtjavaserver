package com.panos;

public class Command {
    public String command;

    // Controller Values
    public float lx;
    public float ly;
    public float rx;
    public float ry;

    // Button values
    public int button;
    public boolean isPressed;

    public String message;

    public Command() {

    }

    public Command(String msg) {
        command = "msg";
        message = msg;
    }
}
