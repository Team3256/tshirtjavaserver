package com.panos.subsystems;

import com.panos.Log;
import com.panos.RobotSerial;
import com.panos.Subsystem;
import com.panos.Utils;

// This class controls the shooter from the Java code
public class Shooter implements Subsystem {
    private RobotSerial serial;

    private boolean reloadingLeft = false;
    private boolean reloadingRight = false;

    public Shooter() {

    }

    public void setPivotSpeed(double speed) {
        serial.sendCommand(">pivot," + speed + ";");
    }

    public void moveToAngle(int angle) {
        Log.robot("MOVING PIVOT TO " + angle + " DEGREES");
        serial.sendCommand(">pivotangle," + angle + ";");
    }

    public void pivotHome() {
        if (!reloadingLeft && !reloadingRight) {
            serial.sendCommand(">pivothome;");
        }
    }

    public void popLeft() {
        if (!reloadingLeft) {
            serial.sendCommand(">popl;");
        }
    }

    public void popRight() {
        if (!reloadingRight) {
            serial.sendCommand(">popr;");
        }
    }

    public void ejectLeft() {
        if (!reloadingLeft) {
            serial.sendCommand(">ejectl;");
        }
    }

    public void ejectRight() {
        if (!reloadingRight) {
            serial.sendCommand(">ejectr;");
        }
    }


    public void retractLeft() {
        if (!reloadingLeft) {
            serial.sendCommand(">retractl;");
        }
    }

    public void retractRight() {
        if (!reloadingRight) {
            serial.sendCommand(">retractr;");
        }
    }

    public void pushLeft() {
        if (!reloadingRight) {
            serial.sendCommand(">pushl;");
        }
    }

    public void pushRight() {
        if (!reloadingRight) {
            serial.sendCommand(">pushr;");
        }
    }

    public void reloadLeft() {
        if (!reloadingLeft) {
            reloadingLeft = true;
            Thread thread = new Thread(() -> {
                try {
                    serial.sendCommand(">popl;");
                    Thread.sleep(500);
                    serial.sendCommand(">ejectl;");
                    Thread.sleep(500);
                    serial.sendCommand(">retractl;");
                    Thread.sleep(500);
                    serial.sendCommand(">pushl;");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadingLeft = false;
            });
            thread.start();
        }
    }

    public void reloadRight() {
        if (!reloadingRight) {
            reloadingRight = true;
            Thread thread = new Thread(() -> {
                try {
                    serial.sendCommand(">popr;");
                    Thread.sleep(500);
                    serial.sendCommand(">ejectr;");
                    Thread.sleep(500);
                    serial.sendCommand(">retractr;");
                    Thread.sleep(500);
                    serial.sendCommand(">pushr;");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadingRight = false;
            });
            thread.start();
        }
    }

    public void shootLeft(int power) {
        serial.sendCommand(">shootl," + power + ";");
    }

    public void shootRight(int power) {
        serial.sendCommand(">shootr," + power + ";");
    }

    @Override
    public void emergencyStop() {
        Log.robot("RETURNING BARRELS TO IDLE POSITION");
        serial.sendCommand(">retractl;>pushl;>retractr;>pushr;");
        Log.robot("MOVING PIVOT TO HOME POSITION");
        pivotHome();
        Utils.delay(3000);
    }

    @Override
    public void setSerial(RobotSerial serial) {
        this.serial = serial;
    }
}
