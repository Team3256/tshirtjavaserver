package com.panos.subsystems;

import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;
import com.panos.utils.Log;

public class SafetyLight implements Subsystem {
    private RobotSerial serial;

    public SafetyLight() {
        serial = RobotSerial.getInstance();
    }

    public void on() {
        serial.sendCommand("lightOn;");
        Log.robot("Turning safety light on");
    }

    public void off() {
        serial.sendCommand("lightOff;");
        Log.robot("Turning safety light off");
    }

    @Override
    public void emergencyStop() {
        this.off();
    }
}
