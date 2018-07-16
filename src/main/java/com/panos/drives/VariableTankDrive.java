package com.panos.drives;

import com.panos.subsystems.Drivetrain;

public class VariableTankDrive extends TankDrive {
    public VariableTankDrive(Drivetrain drivetrain) {
        super(drivetrain);
    }

    @Override
    public void onAxisChange(float lx, float ly, float rx, float ry) {
        // Make sure values don't exceed 0.5 so they don't overpower the motor
        float left = ly * 0.5f;
        float right = ry  * 0.5f * -1;

        if (getPreviousLeft() != left || getPreviousRight() != right) {
            setPreviousLeft(left);
            setPreviousRight(right);
            getDrivetrain().setMotorPower(left, right);
        }
    }
}
