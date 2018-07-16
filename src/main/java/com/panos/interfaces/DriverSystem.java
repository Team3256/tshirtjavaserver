package com.panos.interfaces;

import com.panos.subsystems.Drivetrain;

public interface DriverSystem {
    void onAxisChange(float lx, float ly, float rx, float ry);
    void setDrivetrain(Drivetrain drivetrain);
}
