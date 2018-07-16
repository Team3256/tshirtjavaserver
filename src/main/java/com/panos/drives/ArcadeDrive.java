package com.panos.drives;

import com.panos.interfaces.DriverSystem;
import com.panos.subsystems.Drivetrain;

public class ArcadeDrive implements DriverSystem {
    private Drivetrain drivetrain;
    private float previousLeft = -1;
    private float previousRight = -1;

    public ArcadeDrive() {

    }

    public ArcadeDrive(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    @Override
    public void onAxisChange(float lx, float ly, float rx, float ry) {
        float left = ly;
        float right = rx;
        float leftPower = left + right;
        float rightPower = left - right;

        leftPower = Math.min(Math.max(leftPower, -1), 1);;
        rightPower = Math.min(Math.max(rightPower, -1), 1);;

        leftPower = leftPower * 0.5f;
        rightPower = (rightPower * 0.5f) * -1;

        if (previousLeft != leftPower || previousRight != rightPower) {
            previousLeft = leftPower;
            previousRight = rightPower;
            drivetrain.setMotorPower(leftPower, rightPower);
        }
    }

    @Override
    public void setDrivetrain(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }
}
