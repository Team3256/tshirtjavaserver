package com.panos.subsystems;

import com.panos.utils.Location;

public class RobotLocation {
    private static RobotLocation singleton = null;

    private Location currentLocation;
    private float gyroAngle;
    private float gyroOffset;

    public float getGyroAngle() {
        return gyroAngle + gyroOffset;
    }

    public void setGyroAngle(float angle) {
        gyroAngle = angle;
    }

    public void setGyroOffset(float offset) {
        gyroOffset = offset;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    public static RobotLocation getInstance() {
        if (singleton == null) {
            singleton = new RobotLocation();
        }
        return singleton;
    }
}
