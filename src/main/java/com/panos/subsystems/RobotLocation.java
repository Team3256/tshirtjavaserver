package com.panos.subsystems;

import com.panos.utils.Location;

public class RobotLocation {
    private static RobotLocation singleton = null;

    private Location currentLocation;
    private int gyroAngle;

    public int getGyroAngle() {
        return gyroAngle;
    }

    public void setGyroAngle(int angle) {
        gyroAngle = angle;
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
