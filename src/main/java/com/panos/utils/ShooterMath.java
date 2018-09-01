package com.panos.utils;

public class ShooterMath {
    // Theta
    private static int angle = 45;

    // Alpha
    public static double CalculateAlpha(double x) {
        return x * Math.tan(angle);
    }

    // A
    public static double CalculateA(double x) {
        return 16.1 * (Math.pow(x, 2) / (Math.pow(Math.cos(angle), 2)));
    }

    // Velocity
    public static double CalculateVelocity(double x, double y) {
        return Math.sqrt(-((CalculateA(x) / (y - CalculateAlpha(x)))));
    }
}
