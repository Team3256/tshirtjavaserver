package com.panos;

import jssc.SerialPort;
import jssc.SerialPortException;

public class RobotSerial {
    SerialPort port;

    public RobotSerial() {
//        port = new SerialPort("/dev/ttyACM0");
//
//        try {
//            port.openPort();
//            port.setParams(SerialPort.BAUDRATE_115200,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
    }

    public void sendCommand(String s) {
        System.out.println(s);
//        try {
//            port.writeString(s + "\r\n");
//        } catch (SerialPortException e) {
//            e.printStackTrace();
//        }
    }
}
