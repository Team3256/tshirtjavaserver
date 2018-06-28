package com.panos;

public class Shooter {
    private RobotSerial serial;

    private boolean reloadingLeft = false;

    public Shooter(RobotSerial serial) {
        this.serial = serial;
    }

    public void setPivotSpeed(double speed) {
        serial.sendCommand("p(" + speed + ")");
    }

    public void pivotHome() {
        if (!reloadingLeft) {
            serial.sendCommand("ph()");
        }
    }

    public void popLeft() {
        if (!reloadingLeft) {
            serial.sendCommand("pl()");
        }
    }

    public void ejectLeft() {
        if (!reloadingLeft) {
            serial.sendCommand("el()");
        }
    }

    public void retractLeft() {
        if (!reloadingLeft) {
            serial.sendCommand("rl()");
        }
    }

    public void reloadLeft() {
        if (!reloadingLeft) {
            reloadingLeft = true;
            Thread thread = new Thread(() -> {
                try {
                    serial.sendCommand("pl()");
                    Thread.sleep(500);
                    serial.sendCommand("el()");
                    Thread.sleep(500);
                    serial.sendCommand("rl()");
                    Thread.sleep(500);
                    serial.sendCommand("pul()");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadingLeft = false;
            });
            thread.start();
        }
    }

    public void reloadRight() {
        if (!reloadingLeft) {
            reloadingLeft = true;
            Thread thread = new Thread(() -> {
                try {
                    serial.sendCommand("pr()");
                    Thread.sleep(500);
                    serial.sendCommand("er()");
                    Thread.sleep(500);
                    serial.sendCommand("rr()");
                    Thread.sleep(500);
                    serial.sendCommand("pur()");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reloadingLeft = false;
            });
            thread.start();
        }
    }

    public void shootLeft(int power) {
        serial.sendCommand("sl(" + power + ")");
    }

    public void shootRight(int power) {
        serial.sendCommand("sr(" + power + ")");
    }
}
