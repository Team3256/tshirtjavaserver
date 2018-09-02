package com.panos.utils;

public class ShooterMath {
    // Alpha
    public static double CalculateAlpha(double x) {
        return x;
    }

    // A
    public static double CalculateA(double x) {
        return 16.1 * (Math.pow(x, 2) * 2);
    }

    // Velocity
    public static double CalculateVelocity(double x, double y) {
        return Math.sqrt(-((CalculateA(x) / (y - CalculateAlpha(x)))));
    }
}
