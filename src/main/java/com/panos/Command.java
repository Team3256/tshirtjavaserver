package com.panos;

public class Command {
    public String command;

    // Controller Values
    public double lx;
    public double ly;
    public double rx;
    public double ry;

    // Button values
    public int button;
    public boolean isPressed;

    // Log message
    public String message;

    public long startMs;
    public long endMs;

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

    public Command(long startTime) {
        command = "ping";
        startMs = startTime;
    }

    public Command(long startTime, long endTime) {
        command = "finishPing";
        startMs = startTime;
        endMs = endTime;
    }

    public Command(String msg) {
        command = "log";
        message = msg;
    }

    public Command() {

    }
}
