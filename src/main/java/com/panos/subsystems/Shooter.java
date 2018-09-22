package com.panos.subsystems;

import com.panos.Robot;
import com.panos.utils.Location;
import com.panos.utils.Log;
import com.panos.RobotSerial;
import com.panos.interfaces.Subsystem;
import com.panos.utils.Utils;

// This class controls the shooter from the Java code
public class Shooter implements Subsystem {
    private static Shooter singleton = null;

    private RobotSerial serial;

    private int pivotPosition = 0;
    private int shootingVelocity = 40;

    public Location targetLocation;

    public enum State {
        WANTS_TO_SHOOT_LEFT {
            @Override
            State run(Robot robot) {
                Log.state("Preparing to shoot left");
                return SHOOTING_LEFT;
            }
        },
        WANTS_TO_SHOOT_RIGHT {
            @Override
            State run(Robot robot) {
                Log.state("Preparing to shoot right");
                return SHOOTING_RIGHT;
            }
        },
        TURN_TO_TARGET {
            @Override
            State run(Robot robot) {
                Log.state("Preparing to turn to angle");
                Log.state(robot.getRobotLocation().getCurrentLocation().toString());
                Log.state("Angle: " + robot.getRobotLocation().getCurrentLocation().getAngle(Shooter.getInstance().targetLocation));
                return IDLE;
            }
        },
        SHOOTING_LEFT {
            @Override
            State run(Robot robot) {
                Log.state("Shooting Left");
                robot.getShooter().shootLeft(robot.getShooter().getShootingVelocity());
                return IDLE;
            }
        },
        SHOOTING_RIGHT {
            @Override
            State run(Robot robot) {
                Log.state("Shooting Right");
                robot.getShooter().shootRight(robot.getShooter().getShootingVelocity());
                return IDLE;
            }
        },
        RELOADING_LEFT {
            @Override
            State run(Robot robot) {
                Log.state("Reloading Right");
                robot.getShooter().reloadLeft();
                return IDLE;
            }
        },
        RELOADING_RIGHT {
            @Override
            State run(Robot robot) {
                Log.state("Reloading Right");
                robot.getShooter().reloadRight();
                return IDLE;
            }
        },
        PIVOTING_HOME {
            @Override
            State run(Robot robot) {
                Log.state("Pivoting Home");
                Shooter.getInstance().pivotHome();
                int pivotPos = -1;
//                while (pivotPos != 0) {
//                    Log.arduino("WAITING: " + pivotPos);
//                    Utils.delay(1000);
//                    pivotPos = Shooter.getInstance().pivotPosition;
//                }
                Log.robot("Pivot Homed");
                return IDLE;
            }
        },
        IDLE {
            @Override
            State run(Robot robot) {
                //System.out.println("Idle State");
                return IDLE;
            }
        };

        abstract State run(Robot robot);
    }

    private State state;

    public Shooter() {
        state = State.IDLE;
        serial = RobotSerial.getInstance();
    }

    public void update() {
        state = state.run(Robot.getInstance());
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setPivotPosition(int pos) {
        this.pivotPosition = pos;
    }

    public void setPivotSpeed(double speed) {
        serial.sendCommand("pivot," + speed + ";");
    }

    public void moveToAngle(int angle) {
        Log.robot("Pivoting to " + angle + " degrees");
        serial.sendCommand("updatePivotAngle," + angle + ";");
    }

    public void pivotHome() {
        Log.robot("Pivoting to home");
        serial.sendCommand("pivotHome;");
    }

    public void popLeft() {
        Log.robot("Opening left barrel");
        serial.sendCommand("popLeft;");
    }

    public void popRight() {
        Log.robot("Opening right barrel");
        serial.sendCommand("popRight;");
    }

    public void ejectLeft() {
        Log.robot("Ejecting left cartridge");
        serial.sendCommand("ejectLeft;");
    }

    public void ejectRight() {
        Log.robot("Ejecting right cartridge");
        serial.sendCommand("ejectRight;");
    }


    public void retractLeft() {
        Log.robot("Retracting left actuator");
        serial.sendCommand("retractLeft;");
    }

    public void retractRight() {
        Log.robot("Retracting right actuator");
        serial.sendCommand("retractRight;");
    }

    public void pushLeft() {
        Log.robot("Retracting left barrel");
        serial.sendCommand("pushLeft;");
    }

    public void pushRight() {
        Log.robot("Retracting right barrel");
        serial.sendCommand("pushRight;");
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
        serial.sendCommand("shootLeft," + power + ";");
    }

    public void shootRight(int power) {
        Log.robot("Shooting right barrel with power level " + power);
        serial.sendCommand("shootRight," + power + ";");
    }

    public void setShootingVelocity(int vel) {
        if (vel < 0 || vel > 150) {
            Log.robot("Cannot change velocity to " + vel + "ms");
        } else {
            Log.robot("Changing robot velocity to " + vel + "ms");
            shootingVelocity = vel;
        }
    }

    public int getShootingVelocity() {
        return shootingVelocity;
    }

    @Override
    public void emergencyStop() {
        setState(State.PIVOTING_HOME);
    }

    public static Shooter getInstance() {
        if (singleton == null) {
            singleton = new Shooter();
        }
        return singleton;
    }
}
