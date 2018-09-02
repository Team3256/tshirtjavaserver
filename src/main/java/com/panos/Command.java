package com.panos;

public class Command {
    public String command = "";

    // Controller Values
    public double lx = 0;
    public double ly = 0;
    public double rx = 0;
    public double ry = 0;

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

    public Command(double lx, double ly, double rx, double ry) {
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
