package com.panos.subsystems;

import com.panos.drives.ArcadeDrive;
import com.panos.drives.TankDrive;
import com.panos.drives.VariableTankDrive;
import com.panos.interfaces.DriverSystem;
import com.panos.utils.Log;
import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;

import java.util.ArrayList;

// This class has methods to control driving,
// and communicating that data to the Arduino
public class Drivetrain implements Subsystem {
    private static Drivetrain singleton = null;

    private RobotSerial serial;

    // Get access to serial object to send messages
    public Drivetrain() {
        serial = RobotSerial.getInstance();
    }

    // Send command to Arduino to handle PWM singles
    public void setMotorPower(double left, double right) {
        serial.sendCommand(">motorleft," + left + ";");
        serial.sendCommand(">motorright," + right + ";");
    }

    public void setRightMotorPower(double right) {
        serial.sendCommand(">motorright," + right + ";");
    }

    public void setLeftMotorPower(double left) {
        left = left * -1;
        serial.sendCommand(">motorleft," + left + ";");
    }

    public void drive(double lx, double ly, double rx, double ry) {
        ArcadeDrive.onAxisChange(lx, ly, rx, ry);
    }

    @Override
    public void emergencyStop() {
        Log.robot("Setting motor power to 0");
        setMotorPower(0, 0);
    }

    public static Drivetrain getInstance() {
        if (singleton == null) {
            singleton = new Drivetrain();
        }
        return singleton;
    }
}
