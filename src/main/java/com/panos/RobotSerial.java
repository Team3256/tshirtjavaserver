package com.panos;

import com.fazecast.jSerialComm.SerialPort;
import com.panos.utils.Log;
import java.util.Arrays;

public class RobotSerial {
    private static RobotSerial singleton = null;
    public SerialPort port;

    public enum State {
        IDLE,
        READING_COMMAND,
        READING_PAYLOAD,
        EXECUTE
    }
    State state = State.IDLE;

    private String command = "";
    private String payload = "";

    // Connect to the serial port on the Arduino
    public RobotSerial() {
//        Log.serial("Opening serial port");
//        System.out.println(Arrays.toString(SerialPort.getCommPorts()));
//        port = SerialPort.getCommPort("/dev/ttyACM0");
//        port.setBaudRate(1000000);
//        port.setNumStopBits(2);
//        port.openPort();
//        Thread thread = new Thread(() -> {
//            try {
//                while (true)
//                {
//                    while (port.bytesAvailable() == 0)
//                        Thread.sleep(20);
//
//                    byte[] readBuffer = new byte[port.bytesAvailable()];
//                    int numRead = port.readBytes(readBuffer, 1);
//                    System.out.println("Read " + numRead + " bytes.");
//                    char newChar = (char) readBuffer[0];
//
//                    switch(newChar) {
//                        case '>':
//                            Log.arduino("Reading command from Arduino");
//                            state = state.READING_COMMAND;
//                            continue;
//                        case ':':
//                            Log.arduino("Reading payload from Arduino");
//                            state = State.READING_PAYLOAD;
//                            continue;
//                        case ';':
//                            Log.arduino("Executing command and payload from Arduino");
//                            state = State.EXECUTE;
//                            break;
//                    }
//
//                    switch(state) {
//                        case READING_COMMAND:
//                            command = command + newChar;
//                            break;
//                        case READING_PAYLOAD:
//                            payload = payload + newChar;
//                            break;
//                        case EXECUTE:
//                            Log.arduino("NEW COMMAND: " + command);
//                            Log.arduino("PAYLOAD: " + payload);
//                            executeCommand();
//                            command = "";
//                            payload = "";
//                            state = State.IDLE;
//                            break;
//                    }
//                }
//            } catch (Exception e) { e.printStackTrace(); }
//        });
//        thread.start();
    }

    private void executeCommand() {
        switch (command) {
            case "log":
                Log.arduino(payload);
                break;
            case "updatePivotPos":
                Log.arduino("Pivot Position Update Received");
                Robot.getInstance().getShooter().setPivotPosition(Integer.valueOf(payload));
                break;
            case "lat":
                Log.arduino("New GPS Latitude Received");
                Robot.getInstance().getRobotLocation().getCurrentLocation().setLatitude(Double.valueOf(payload));
                break;
            case "lon":
                Log.arduino("New GPS Longitude Received");
                Robot.getInstance().getRobotLocation().getCurrentLocation().setLatitude(Double.valueOf(payload));
                break;
            case "offset":
                Log.arduino("New Gyro Offset Received");
                Robot.getInstance().getRobotLocation().setGyroOffset(Float.valueOf(payload));
                break;
        }
        Log.arduino("Command executed");
    }

    public void sendCommand(String s) {
        Log.serial("Sending: " + s);
        //port.writeBytes(s.getBytes(), s.getBytes().length);
    }

    public static RobotSerial getInstance() {
        if (singleton == null) {
            singleton = new RobotSerial();
        }
        return singleton;
    }
}
