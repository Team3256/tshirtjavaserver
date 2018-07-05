package com.panos;

import com.panos.subsystems.Drivetrain;
import com.panos.subsystems.Shooter;

import static com.panos.ButtonType.*;

public class Robot implements ControllerListener {
    private RobotSerial serial;
    private Drivetrain drivetrain;
    private Shooter shooter;

    private float previousLeft = -1;
    private float previousRight = -1;

    // Init subsystems
    public Robot() {
        serial = new RobotSerial();
        drivetrain = new Drivetrain(serial);
        shooter = new Shooter(serial);
    }

    // Run things when the joysticks change
    public void onAxisChange(float lx, float ly, float rx, float ry) {
        //arcadeDrive(ly, rx);
        tankDrive(ly, ry);
    }

    // Run things on the button press
    public void onButtonPress(int button, boolean isPressed) {
        switch (button) {
            case A:
                if (isPressed) {
                    Log.robot("A Pressed");
                } else {
                    Log.robot("A Depressed");
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
            case ClickRight:
                if (isPressed) {
                    shooter.popRight();
                }
                else {
                    shooter.retractRight();
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
                break;
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

    public void arcadeDrive(float left, float right) {
        if (previousLeft != left || previousRight != right) {
            Log.robot("Throttle: " + left);
            Log.robot("Turn: " + right);
            previousLeft = left;
            previousRight = right;
            drivetrain.setMotorPower(left, right);
        }
    }

    public void tankDrive(float left, float right) {
        left = left > 0 ? 0.5F : (left < 0 ? -0.5F : 0);
        right = right > 0 ? -0.5F : (right < 0 ? 0.5F : 0);
        if (previousLeft != left || previousRight != right) {
            previousLeft = left;
            previousRight = right;
            drivetrain.setMotorPower(left, right);
        }
    }
}
