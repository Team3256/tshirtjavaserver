package com.panos.subsystems;

import com.panos.Log;
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

    public void arcadeDrive(float left, float right) {
        float leftPower = left + right;
        float rightPower = left - right;

        leftPower = Math.min(Math.max(leftPower, -1), 1);;
        rightPower = Math.min(Math.max(rightPower, -1), 1);;

        leftPower = leftPower * 0.5f;
        rightPower = (rightPower * 0.5f) * -1;

        if (previousLeft != leftPower || previousRight != rightPower) {
            previousLeft = leftPower;
            previousRight = rightPower;
            setMotorPower(leftPower, rightPower);
        }
    }

    @Override
    public void emergencyStop() {
        Log.robot("SETTING MOTOR POWER TO 0");
        setMotorPower(0, 0);
    }

    @Override
    public void setSerial(RobotSerial serial) {
        this.serial = serial;
    }
}
