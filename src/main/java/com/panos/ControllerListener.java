package com.panos;

// Interface for Controllers, in case we use different controllers
public interface ControllerListener {
    void onAxisChange(float lx, float ly, float rx, float ry);

    void onButtonPress(int button, boolean isPressed);
}
