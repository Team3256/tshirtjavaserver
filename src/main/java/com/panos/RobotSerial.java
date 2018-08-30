package com.panos;

import com.panos.utils.Log;

import java.io.*;

public class RobotSerial {
    public OutputStream port;

    // Connect to the serial port on the Arduino
    public RobotSerial() {
        // Get sysfs serial port
        File initialFile = new File("/dev/ttyACM0");
        try {
            // Get an input stream so we can get what the Arduino prints out
            InputStream targetStream = new FileInputStream(initialFile);
            // Make a new output stream so we can send to the Arduino
            port = new FileOutputStream(initialFile);
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        if (targetStream.available() > 1) {
                            Log.arduino(Character.toString((char) targetStream.read()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            Log.serial("SERIAL PORT OPENED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String s) {
        Log.serial("Sending: " + s);
        try {
            port.write(s.getBytes());
            //port.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
