package com.panos.drives;

import com.panos.interfaces.DriverSystem;
import com.panos.subsystems.Drivetrain;

public class TankDrive implements DriverSystem {
    private Drivetrain drivetrain;
    private float previousLeft = -1;
    private float previousRight = -1;

    public TankDrive() {
        drivetrain = Drivetrain.getInstance();
    }

    public void setPreviousLeft(float previousLeft) {
        this.previousLeft = previousLeft;
    }

    public void setPreviousRight(float previousRight) {
        this.previousRight = previousRight;
    }

    public float getPreviousLeft() {
        return previousLeft;
    }

    public float getPreviousRight() {
        return previousRight;
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    @Override
    public void onAxisChange(float lx, float ly, float rx, float ry) {
        // Make sure values don't exceed 0.5 so they don't overpower the motor
        float left = ly > 0 ? 0.5F : (ly < 0 ? -0.5F : 0);
        float right = ry > 0 ? -0.5F : (ry < 0 ? 0.5F : 0);

        if (previousLeft != left || previousRight != right) {
            previousLeft = left;
            previousRight = right;
            drivetrain.setMotorPower(left, right);
        }
    }
}
