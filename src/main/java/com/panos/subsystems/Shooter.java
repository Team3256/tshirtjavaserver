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
        serial.sendCommand(">p," + speed + ";");
    }

    public void pivotHome() {
        if (!reloadingLeft && !reloadingRight) {
            serial.sendCommand(">ph;");
        }
    }

    public void popLeft() {
        if (!reloadingLeft) {
            serial.sendCommand(">pl;");
        }
    }

    public void popRight() {
        if (!reloadingRight) {
            serial.sendCommand(">pr;");
        }
    }

    public void ejectLeft() {
        if (!reloadingLeft) {
            serial.sendCommand(">el;");
        }
    }

    public void ejectRight() {
        if (!reloadingRight) {
            serial.sendCommand(">er;");
        }
    }


    public void retractLeft() {
        if (!reloadingLeft) {
            serial.sendCommand(">rl;");
        }
    }

    public void retractRight() {
        if (!reloadingRight) {
            serial.sendCommand(">rr;");
        }
    }

    public void reloadLeft() {
        if (!reloadingLeft) {
            reloadingLeft = true;
            Thread thread = new Thread(() -> {
                try {
                    serial.sendCommand(">pl;");
                    Thread.sleep(500);
                    serial.sendCommand(">el;");
                    Thread.sleep(500);
                    serial.sendCommand(">rl;");
                    Thread.sleep(500);
                    serial.sendCommand(">pul;");
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
                    serial.sendCommand(">pr;");
                    Thread.sleep(500);
                    serial.sendCommand(">er;");
                    Thread.sleep(500);
                    serial.sendCommand(">rr;");
                    Thread.sleep(500);
                    serial.sendCommand(">pur;");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadingRight = false;
            });
            thread.start();
        }
    }

    public void shootLeft(int power) {
        serial.sendCommand(">sl," + power + ";");
    }

    public void shootRight(int power) {
        serial.sendCommand(">sr," + power + ";");
    }
}
