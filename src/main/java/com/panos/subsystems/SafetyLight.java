package com.panos.subsystems;

import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;
import com.panos.utils.Log;
import com.panos.utils.Utils;

public class SafetyLight implements Subsystem {
    private RobotSerial serial;
    private volatile boolean isBlinking = false;
    private volatile boolean isOn = true;

    public SafetyLight() {
//        Thread thread = new Thread(() - {
//            while(true) {
//                if (this.isBlinking) {
//                    if (this.isOn) {
//                        this.off();
//                    } else {
//                        this.on();
//                    }
//                }
//                Utils.delay(400);
//            }
//        });
//        thread.start();
        serial = RobotSerial.getInstance();
    }

    public void on() {
        serial.sendCommand("lighton;");
        Log.robot("Turning safety light on");
        this.isOn = true;
    }

    public void off() {
        serial.sendCommand("lightoff;");
        Log.robot("Turning safety light off");
        this.isOn = false;
    }

    public void setIsBlinking(boolean blink) {
        this.isBlinking = blink;
    }

    @Override
    public void emergencyStop() {
        this.setIsBlinking(false);
        this.on();
    }
}
