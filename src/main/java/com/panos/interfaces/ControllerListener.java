package com.panos.interfaces;

// Interface for Controllers, in case we use different controllers
public interface ControllerListener {
    void onAxisChange(double lx, double ly, double rx, double ry);

    void onButtonPress(int button, boolean isPressed);
}
