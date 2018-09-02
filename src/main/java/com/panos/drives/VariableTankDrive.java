package com.panos.drives;

import com.panos.subsystems.Drivetrain;

public class VariableTankDrive extends TankDrive {
    @Override
    public void onAxisChange(float lx, float ly, float rx, float ry) {
        // Make sure values don't exceed 0.5 so they don't overpower the motor
        float left = ly * 0.5f;
        float right = ry  * 0.5f * -1;

        left = Math.min(Math.max(left, -0.5f), 0.5f);
        right = Math.min(Math.max(right, -0.5f), 0.5f);

        if (left > getPreviousLeft() + 0.05 || left < getPreviousLeft() - 0.05) {
            getDrivetrain().setLeftMotorPower(left);
            setPreviousLeft(left);
        }

        if (right > getPreviousRight() + 0.05 || right < getPreviousRight() - 0.05) {
            getDrivetrain().setRightMotorPower(right);
            setPreviousRight(right);
        }
    }
}
