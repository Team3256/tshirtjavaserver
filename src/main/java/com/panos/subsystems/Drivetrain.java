package com.panos.subsystems;

import com.panos.drives.ArcadeDrive;
import com.panos.drives.TankDrive;
import com.panos.drives.VariableTankDrive;
import com.panos.interfaces.DriverSystem;
import com.panos.utils.Log;
import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

// This class has methods to control driving,
// and communicating that data to the Arduino
public class Drivetrain implements Subsystem {
    private RobotSerial serial;
    private ArrayList<DriverSystem> driverSystems;
    private int selectedDriverSystem;

    // Get access to serial object to send messages
    public Drivetrain() {
        driverSystems = new ArrayList<>();
        driverSystems.add(new TankDrive(this));
        driverSystems.add(new ArcadeDrive(this));
        driverSystems.add(new VariableTankDrive(this));
        selectedDriverSystem = 0;
    }

    // Send command to Arduino to handle PWM singles
    public void setMotorPower(float left, float right) {
        serial.sendCommand(">motorleft," + left + ";");
        serial.sendCommand(">motorright," + right + ";");
    }

    public ArrayList<DriverSystem> getDriverSystems() {
        return driverSystems;
    }

    public void drive(float lx, float ly, float rx, float ry) {
        driverSystems.get(selectedDriverSystem).onAxisChange(lx, ly, rx, ry);
    }

    @Override
    public void emergencyStop() {
        Log.robot("Setting motor power to 0");
        setMotorPower(0, 0);
    }

    @Override
    public void setSerial(RobotSerial serial) {
        this.serial = serial;
    }

    public void changeDrive() {
        selectedDriverSystem = selectedDriverSystem + 1 == driverSystems.size() ? 0 : selectedDriverSystem + 1;
        Log.robot("Setting drive system to " + driverSystems.get(selectedDriverSystem).getClass().getSimpleName());
    }
}
