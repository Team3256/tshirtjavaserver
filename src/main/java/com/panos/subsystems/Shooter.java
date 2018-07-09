package com.panos.subsystems;

import com.panos.RobotSerial;

// This class controls the shooter from the Java code
public class Shooter {
    private RobotSerial serial;

    private boolean reloadingLeft = false;
    private boolean reloadingRight = false;

    public Shooter(RobotSerial serial) {
        this.serial = serial;
    }

    public void setPivotSpeed(double speed) {
        serial.sendCommand(">pivot," + speed + ";");
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
}
