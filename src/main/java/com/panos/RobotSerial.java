package com.panos;

import java.io.*;

public class RobotSerial {
    public OutputStream port;

    // Connect to the serial port on the Arduino
    public RobotSerial() {
        // Get a list of all Serial ports on the device
        File initialFile = new File("/dev/ttyACM0");
        try {
            InputStream targetStream = new FileInputStream(initialFile);
            port = new FileOutputStream(initialFile);
            java.util.Scanner s = new java.util.Scanner(targetStream).useDelimiter("\r\n");
            Thread thread = new Thread(() -> {
                while (true) {
                    Log.arduino(s.hasNext() ? s.next() : "");
                }
            });
            thread.start();
            Log.serial("SERIAL PORT OPENED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void serialEvent(SerialPortEvent event) {
//        if (event.isRXCHAR() && event.getEventValue() > 0) {
//            int bytesCount = event.getEventValue();
//            try {
//                Log.arduino(port.readString(bytesCount));
//            } catch (SerialPortException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
    public void sendCommand(String s) {
        Log.serial("SENDING: " + s);
        try {
            port.write(s.getBytes());
            port.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
