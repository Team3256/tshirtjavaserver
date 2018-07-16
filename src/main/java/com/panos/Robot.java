package com.panos;

import com.panos.interfaces.ControllerListener;
import com.panos.subsystems.Drivetrain;
import com.panos.subsystems.Shooter;
import com.panos.utils.Log;

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
        drivetrain.arcadeDrive(ly, rx);
    }

    // Run things on the button press
    public void onButtonPress(int button, boolean isPressed) {
        switch (button) {
            case A:
                if (isPressed)
                    shooter.moveToAngle(30);
                break;
            case B:
                if (isPressed)
                    shooter.moveToAngle(35);
                break;
            case X:
                if (isPressed)
                    shooter.moveToAngle(40);
                break;
            case Y:
                if (isPressed)
                    shooter.moveToAngle(45);
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
                    shooter.pushLeft();
                }
                break;
            case ClickRight:
                if (isPressed) {
                    shooter.popRight();
                }
                else {
                    shooter.pushRight();
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
        Log.robot("Stopping robot...");
        drivetrain.emergencyStop();
        shooter.emergencyStop();
        Log.robot("Robot successfully disabled");
    }
}
