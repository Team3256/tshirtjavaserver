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

    public Command(float lx, float ly, float rx, float ry) {
        command = "axis";
        this.lx = lx;
        this.ly = ly;
        this.rx = rx;
        this.ry = ry;
    }

    public Command(int button, boolean isPressed) {
        command = "button";
        this.button = button;
        this.isPressed = isPressed;
    }
}
