package com.panos;

import com.panos.subsystems.Shooter;
import com.panos.utils.Log;
import com.panos.utils.Utils;

import java.io.*;

public class RobotSerial {
    public OutputStream port;

    public enum State {
        IDLE,
        READING_COMMAND,
        READING_PAYLOAD,
        EXECUTE
    }

    Shooter shooter;

    // Connect to the serial port on the Arduino
    public RobotSerial() {
        // Get sysfs serial port
//        File initialFile = new File("/dev/ttyACM0");
//        try {
//            // Get an input stream so we can get what the Arduino prints out
//            // Make a new output stream so we can send to the Arduino
//            port = new FileOutputStream(initialFile);
//            BufferedReader br = new BufferedReader(new FileReader(initialFile));
//            Thread thread = new Thread(() -> {
//                State state = State.IDLE;
//                String command = "";
//                String payload = "";
//                while (true) {
//                    try {
//                        int charInt;
//                        if ((charInt = br.read()) != -1) {
//                            char newChar = (char) charInt;
//
//                            if (newChar == '>') {
//                                Log.arduino("New command from Arduino");
//                                state = State.READING_COMMAND;
//                                continue;
//                            }
//
//                            if (newChar == ':') {
//                                state = State.READING_PAYLOAD;
//                                continue;
//                            }
//
//                            if (newChar == ';') {
//                                state = State.EXECUTE;
//                            }
//
//                            if (state.equals(State.READING_COMMAND)) {
//                                command = command + newChar;
//                            }
//
//                            if (state.equals(State.READING_PAYLOAD)) {
//                                payload = payload + newChar;
//                            }
//
//                            if (state.equals(State.EXECUTE)) {
//                                Log.arduino("NEW COMMAND: " + command);
//                                Log.arduino("PAYLOAD: " + payload);
//
//                                switch (command) {
//                                    case "updatePivotPos":
//                                        Log.arduino("Pivot Position Update Received");
//                                        shooter.setPivotPosition(Integer.valueOf(payload));
//                                        break;
//                                }
//                                Log.arduino("Command Complete");
//                                state = State.IDLE;
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.start();
//            Log.serial("SERIAL PORT OPENED");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void sendCommand(String s) {
        Log.serial("Sending: " + s);
//        try {
//            //port.write(s.getBytes());
//            //port.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
