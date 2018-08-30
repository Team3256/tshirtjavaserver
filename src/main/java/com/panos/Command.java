package com.panos;

public class Command {
    public String command = "";

    // Controller Values
    public float lx = 0;
    public float ly = 0;
    public float rx = 0;
    public float ry = 0;

    // Button values
    public int button = -1;
    public boolean isPressed = false;

    public String message = "";

    public int version = -1;
    public double pivotAcceleration = 0;

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
