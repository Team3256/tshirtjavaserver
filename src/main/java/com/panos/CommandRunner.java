package com.panos;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class CommandRunner {
    String[] ports;
    SerialPort port;
    boolean initSerialPort = false;

    public CommandRunner() {
        ports = SerialPortList.getPortNames();

        if (initSerialPort) {
            port = new SerialPort("/dev/ttyACM0");
            try {
                port.openPort();
                port.setParams(SerialPort.BAUDRATE_115200,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

    public void runCommand(Command command) {
//        if (command.getCommand().equals("INVALID")) {
//            System.out.println("Invalid command.");
//        }
//
//        if (port.isOpened()) {
//            try {
//                System.out.println("SENDING");
//                port.writeString(command.getCommand() + "(" + command.getParam() + ")");
//            } catch (SerialPortException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
