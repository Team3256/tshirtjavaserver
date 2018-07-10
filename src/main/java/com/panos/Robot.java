package com.panos;

import com.panos.subsystems.Drivetrain;
import com.panos.subsystems.Shooter;

import static com.panos.constants.ButtonType.*;

public class Robot implements ControllerListener {
    private RobotSerial serial;
    private Drivetrain drivetrain;
    private Shooter shooter;

    // Init subsystems
    public Robot() {
        serial = new RobotSerial();

        drivetrain = new Drivetrain();
        shooter = new Shooter();

        drivetrain.setSerial(serial);
        shooter.setSerial(serial);
    }

    // Run things when the joysticks change
    public void onAxisChange(float lx, float ly, float rx, float ry) {
        //arcadeDrive(ly, rx);
        drivetrain.tankDrive(ly, ry);
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
        drivetrain.emergencyStop();
        shooter.emergencyStop();
    }
}
