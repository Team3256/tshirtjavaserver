package com.panos.subsystems;

import com.panos.RobotSerial;
import com.panos.Subsystem;

// This class has methods to control driving,
// and communicating that data to the Arduino
public class Drivetrain implements Subsystem {
    private RobotSerial serial;

    private float previousLeft = -1;
    private float previousRight = -1;

    // Get access to serial object to send messages
    public Drivetrain() {}

    // Send command to Arduino to handle PWM singles
    public void setMotorPower(float left, float right) {
        serial.sendCommand(">motorleft," + left + ";");
        serial.sendCommand(">motorright," + right + ";");
    }

    public void tankDrive(float left, float right) {
        // Make sure values don't exceed 0.5 so they don't overpower the motor
        left = left > 0 ? 0.5F : (left < 0 ? -0.5F : 0);
        right = right > 0 ? -0.5F : (right < 0 ? 0.5F : 0);

        if (previousLeft != left || previousRight != right) {
            previousLeft = left;
            previousRight = right;
            setMotorPower(left, right);
        }
    }

    @Override
    public void emergencyStop() {
        setMotorPower(0, 0);
    }

    @Override
    public void setSerial(RobotSerial serial) {
        this.serial = serial;
    }
}
