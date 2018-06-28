package com.panos;

public interface ControllerListener {
    void onAxisChange(float lx, float ly, float rx, float ry);

    void onButtonPress(int button, boolean isPressed);
}
