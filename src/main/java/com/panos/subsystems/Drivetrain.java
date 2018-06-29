package com.panos.subsystems;

import com.panos.RobotSerial;

public class Drivetrain {
    private RobotSerial serial;

    public Drivetrain(RobotSerial serial) {
        this.serial = serial;
    }

    public void setMotorPower(float left, float right) {
        serial.sendCommand("m(" + left + ", " + right + ")");
    }
}
