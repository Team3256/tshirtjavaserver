package com.panos.subsystems;

import com.panos.drives.ArcadeDrive;
import com.panos.utils.Log;
import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;

// This class has methods to control driving,
// and communicating that data to the Arduino
public class Drivetrain implements Subsystem {
    private static Drivetrain singleton = null;

    private RobotSerial serial;

    private String previousLeft = "";
    private String previousRight = "";

    // Get access to serial object to send messages
    public Drivetrain() {
        serial = RobotSerial.getInstance();
    }

    // Send command to Arduino to handle PWM singles
    public void setMotorPower(double left, double right) {
        left *= -1;
        String leftCommand = String.format("%.3f", left);
        String rightCommand = String.format("%.3f", right);

        if (!previousLeft.equals(leftCommand) || !previousRight.equals(rightCommand)) {
            serial.sendCommand(">motorleft," + leftCommand + ";");
            serial.sendCommand(">motorright," + rightCommand + ";");
            previousLeft = leftCommand;
            previousRight = rightCommand;
        }
    }

    public void drive(double lx, double ly, double rx, double ry) {
        ArcadeDrive.onAxisChange(lx, ly, rx, ry);
    }

    @Override
    public void emergencyStop() {
        Log.robot("Setting motor power to 0");
        setMotorPower(0.0, 0.0);
    }

    public static Drivetrain getInstance() {
        if (singleton == null) {
            singleton = new Drivetrain();
        }
        return singleton;
    }
}
