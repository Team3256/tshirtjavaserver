package com.panos.subsystems;

import com.panos.utils.Log;
import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;
import com.panos.utils.Utils;

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
        Log.robot("Pivoting to " + angle + " degrees");
        serial.sendCommand(">pivotangle," + angle + ";");
    }

    public void pivotHome() {
        if (!reloadingLeft && !reloadingRight) {
            Log.robot("Pivoting to home");
            serial.sendCommand(">pivothome;");
        }
    }

    public void popLeft() {
        Log.robot("Opening left barrel");
        serial.sendCommand(">popl;");
    }

    public void popRight() {
        Log.robot("Opening right barrel");
        serial.sendCommand(">popr;");
    }

    public void ejectLeft() {
        Log.robot("Ejecting left cartridge");
        serial.sendCommand(">ejectl;");
    }

    public void ejectRight() {
        Log.robot("Ejecting right cartridge");
        serial.sendCommand(">ejectr;");
    }


    public void retractLeft() {
        Log.robot("Retracting left actuator");
        serial.sendCommand(">retractl;");
    }

    public void retractRight() {
        Log.robot("Retracting right actuator");
        serial.sendCommand(">retractr;");
    }

    public void pushLeft() {
        Log.robot("Retracting left barrel");
        serial.sendCommand(">pushl;");
    }

    public void pushRight() {
        Log.robot("Retracting right barrel");
        serial.sendCommand(">pushr;");
    }

    public void reloadLeft() {
        if (!reloadingLeft) {
            reloadingLeft = true;
            Thread thread = new Thread(() -> {
                popLeft();
                Utils.delay(200);
                ejectLeft();
                Utils.delay(300);
                retractLeft();
                Utils.delay(300);
                pushLeft();
                reloadingLeft = false;
            });
            thread.start();
        }
    }

    public void reloadRight() {
        if (!reloadingRight) {
            reloadingRight = true;
            Thread thread = new Thread(() -> {
                popRight();
                Utils.delay(200);
                ejectRight();
                Utils.delay(300);
                retractRight();
                Utils.delay(300);
                pushRight();
                reloadingRight = false;
            });
            thread.start();
        }
    }

    public void shootLeft(int power) {
        Log.robot("Shooting right barrel with power level " + power);
        serial.sendCommand(">shootl," + power + ";");
    }

    public void shootRight(int power) {
        Log.robot("Shooting left barrel with power level " + power);
        serial.sendCommand(">shootr," + power + ";");
    }

    @Override
    public void emergencyStop() {
        Log.robot("Moving barrels to idle position");
        serial.sendCommand(">retractl;>pushl;>retractr;>pushr;");
        pivotHome();
        Utils.delay(3000);
    }

    @Override
    public void setSerial(RobotSerial serial) {
        this.serial = serial;
    }
}
