package com.panos.subsystems;

import com.panos.utils.Log;
import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;
import com.panos.utils.Utils;

// This class controls the shooter from the Java code
public class Shooter implements Subsystem {
    private RobotSerial serial;

    private int pivotPosition = 0;

    public enum State {
        WANTS_TO_SHOOT_LEFT {
            @Override
            State run(Shooter shooter) {
                Log.state("Preparing to shoot left");
                return SHOOTING_LEFT;
            }
        },
        WANTS_TO_SHOOT_RIGHT {
            @Override
            State run(Shooter shooter) {
                Log.state("Preparing to shoot right");
                return SHOOTING_RIGHT;
            }
        },
        SHOOTING_LEFT {
            @Override
            State run(Shooter shooter) {
                Log.state("Shooting Left");
                shooter.shootLeft(40);
                return IDLE;
            }
        },
        SHOOTING_RIGHT {
            @Override
            State run(Shooter shooter) {
                Log.state("Shooting Right");
                shooter.shootRight(40);
                return IDLE;
            }
        },
        RELOADING_LEFT {
            @Override
            State run(Shooter shooter) {
                Log.state("Reloading Right");
                shooter.reloadLeft();
                return IDLE;
            }
        },
        RELOADING_RIGHT {
            @Override
            State run(Shooter shooter) {
                Log.state("Reloading Right");
                shooter.reloadRight();
                return IDLE;
            }
        },
        PIVOTING_HOME {
            @Override
            State run(Shooter shooter) {
                Log.state("Pivoting Home");
                shooter.pivotHome();
                int pivotPos = -1;
                while (pivotPos != 0) {
                    Log.arduino("WAITING: " + pivotPos);
                    Utils.delay(1000);
                    pivotPos = shooter.pivotPosition;
                }
                Log.robot("Pivot Homed");
                return IDLE;
            }
        },
        IDLE {
            @Override
            State run(Shooter shooter) {
                //System.out.println("Idle State");
                return IDLE;
            }
        };

        abstract State run(Shooter shooter);
    }

    private State state;

    public Shooter() {
        state = State.IDLE;
    }

    public void update() {
        state = state.run(this);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setPivotPosition(int pos) {
        this.pivotPosition = pos;
    }

    public void setPivotSpeed(double speed) {
        serial.sendCommand(">pivot," + speed + ";");
    }

    public void moveToAngle(int angle) {
        Log.robot("Pivoting to " + angle + " degrees");
        serial.sendCommand(">pivotangle," + angle + ";");
    }

    public void pivotHome() {
        Log.robot("Pivoting to home");
        serial.sendCommand(">pivothome;");
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
        popLeft();
        Utils.delay(200);
        ejectLeft();
        Utils.delay(300);
        retractLeft();
        Utils.delay(300);
        pushLeft();
        Utils.delay(200);
    }

    public void reloadRight() {
        popRight();
        Utils.delay(200);
        ejectRight();
        Utils.delay(300);
        retractRight();
        Utils.delay(300);
        pushRight();
        Utils.delay(200);
    }

    public void shootLeft(int power) {
        Log.robot("Shooting left barrel with power level " + power);
        serial.sendCommand(">shootl," + power + ";");
    }

    public void shootRight(int power) {
        Log.robot("Shooting right barrel with power level " + power);
        serial.sendCommand(">shootr," + power + ";");
    }

    @Override
    public void emergencyStop() {
        setState(State.PIVOTING_HOME);
    }

    @Override
    public void setSerial(RobotSerial serial) {
        this.serial = serial;
    }
}
