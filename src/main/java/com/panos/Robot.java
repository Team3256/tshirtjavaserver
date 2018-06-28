package com.panos;

import static com.panos.ButtonType.*;

public class Robot implements ControllerListener {
    private RobotSerial serial;
    private Drivetrain drivetrain;
    private Shooter shooter;

    private float lastThrottle = -1;
    private float lastTurn = -1;

    public Robot() {
        serial = new RobotSerial();
        drivetrain = new Drivetrain(serial);
        shooter = new Shooter(serial);
    }

    public void onAxisChange(float lx, float ly, float rx, float ry) {
        arcadeDrive(ly, rx);
    }

    public void onButtonPress(int button, boolean isPressed) {
        switch (button) {
            case A:
                if (isPressed) {
                    System.out.println("A");
                } else {
                    System.out.println("Not A");
                }
                break;
            case DPadUp:
                if (isPressed) {
                    shooter.setPivotSpeed(-0.5);
                } else {
                    shooter.setPivotSpeed(0);
                }
                break;
            case DPadDown:
                if (isPressed) {
                    shooter.setPivotSpeed(0.5);
                } else {
                    shooter.setPivotSpeed(0);
                }
                break;
            case Select:
                if (isPressed)
                    shooter.pivotHome();
                break;
            case ClickLeft:
                if (isPressed) {
                    shooter.popLeft();
                } else {
                    shooter.retractLeft();
                }
                break;
            case LeftShoulder:
                if (isPressed) {
                    shooter.reloadLeft();
                }
                break;
            case RightShoulder:
                if (isPressed) {
                    shooter.reloadRight();
                }
            case LeftTrigger:
                if (isPressed) {
                    shooter.shootLeft(40);
                }
                break;
            case RightTrigger:
                if (isPressed) {
                    shooter.shootRight(40);
                }
                break;
        }
    }

    public void emergencyStop() {
        drivetrain.setMotorPower(0, 0);
    }

    public void arcadeDrive(float throttle, float turn) {
        //System.out.println(throttle);
        System.out.println("Throttle: " + throttle);
        System.out.println("Turn: " + turn);
        if (throttle > 0.3) {

        }
    }
}
