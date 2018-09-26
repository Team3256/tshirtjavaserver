package com.panos;

import com.panos.interfaces.ControllerListener;
import com.panos.subsystems.Drivetrain;
import com.panos.subsystems.RobotLocation;
import com.panos.subsystems.SafetyLight;
import com.panos.subsystems.Shooter;
import com.panos.utils.Location;
import com.panos.utils.Log;

import java.util.Timer;
import java.util.TimerTask;

import static com.panos.constants.ButtonType.*;

public class Robot extends TimerTask implements ControllerListener {
    private static Robot singleton = null;

    private RobotSerial serial;
    private Drivetrain drivetrain;
    private Shooter shooter;
    private SafetyLight safetyLight;

    private RobotLocation robotLocation;

    // Init subsystems
    public Robot() {
        serial = RobotSerial.getInstance();
        drivetrain = Drivetrain.getInstance();
        shooter = Shooter.getInstance();
        safetyLight = new SafetyLight();
        robotLocation = RobotLocation.getInstance();

        Location location = new Location(37.068433, -121.551317);
        robotLocation.setCurrentLocation(location);

        Log.arduino("Waiting 120 seconds for Arduino to finish starting up");
        //Utils.delay(120 * 1000);
        Timer timer = new Timer();
        timer.schedule(this, 0, 5);
    }

    public void run() {
        shooter.update();
    }

    // Run things when the joysticks change
    public void onAxisChange(double lx, double ly, double rx, double ry) {
        drivetrain.drive(lx, ly, rx, ry);
    }

    // Run things on the button press
    public void onButtonPress(int button, boolean isPressed) {
        switch (button) {
            case A:
                if (isPressed)
                    Log.log("CONTROLLER", "A Pressed");
                break;
            case B:
                if (isPressed)
                    Log.log("CONTROLLER", "B Pressed");
                break;
            case X:
                if (isPressed)
                    Log.log("CONTROLLER", "X Pressed");
                break;
            case Y:
                if (isPressed) {
                    Log.log("CONTROLLER", "Y Pressed");
                    shooter.moveToAngle(45);
                }
                break;
            case DPadUp:
                if (isPressed) {
                    //shooter.setPivotSpeed(-0.5);
                    shooter.setShootingVelocity(shooter.getShootingVelocity() + 10);
                } else {
                    //shooter.setPivotSpeed(0);
                }
                break;
            case DPadDown:
                if (isPressed) {
                    //shooter.setPivotSpeed(0.5);
                    shooter.setShootingVelocity(shooter.getShootingVelocity() - 10);
                } else {
                   //shooter.setPivotSpeed(0);
                }
                break;
            case Select:
                if (isPressed)
                    shooter.setState(Shooter.State.PIVOTING_HOME);
                break;
            case Start:
                if (isPressed)
                    Log.log("CONTROLLER", "Start Pressed");
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
                    shooter.setState(Shooter.State.RELOADING_LEFT);
                }
                break;
            case RightShoulder:
                if (isPressed) {
                    shooter.setState(Shooter.State.RELOADING_RIGHT);
                }
                break;
            case LeftTrigger:
                if (isPressed) {
                    shooter.setState(Shooter.State.WANTS_TO_SHOOT_LEFT);
                }
                break;
            case RightTrigger:
                if (isPressed) {
                    shooter.setState(Shooter.State.WANTS_TO_SHOOT_RIGHT);
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

    public void onConnect() {
        safetyLight.setIsBlinking(true);
    }

    public Shooter getShooter() {
        return shooter;
    }

    public RobotLocation getRobotLocation() {
        return robotLocation;
    }

    public static Robot getInstance() {
        if (singleton == null) {
            singleton = new Robot();
        }
        return singleton;
    }
}
