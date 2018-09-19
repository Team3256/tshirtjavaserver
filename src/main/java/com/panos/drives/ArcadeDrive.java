package com.panos.drives;

import com.panos.subsystems.Drivetrain;
import com.panos.utils.Utils;

public class ArcadeDrive {
    private static Drivetrain drivetrain = Drivetrain.getInstance();
    private static final double logitechDeadband = 0.15;

    public static void onAxisChange(double lx, double ly, double rx, double ry) {
        double throttle = ly;
        double turn = rx;

        if (Math.abs(throttle) <= logitechDeadband){
            throttle = 0;
        }
        if (Math.abs(turn) <= logitechDeadband){
            turn = 0;
        }

        double left = throttle + turn;
        double right = throttle - turn;

        left = clamp(left);
        right = clamp(right);

        drivetrain.setMotorPower(left, right);
    }

    public static double clamp(double val){
        return Math.max(Math.min(val, 1.0), -1.0);
    }
}
