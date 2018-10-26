package com.panos.drives;

import com.panos.subsystems.Drivetrain;
import com.panos.utils.Log;

public class TankDrive {
    private static Drivetrain drivetrain = Drivetrain.getInstance();
    private static final double logitechDeadband = 0.15;
    private static double previousLeft = 0;
    private static double previousRight = 0;

    public static void onAxisChange(double lx, double ly, double rx, double ry) {
        Log.robot("ly: " + ly);
        Log.robot("ry: " + ry);
        drivetrain.setMotorPower(ly, ry);
    }

    public static double clamp(double val){
        return Math.max(Math.min(val, 1.0), -1.0);
    }
}
