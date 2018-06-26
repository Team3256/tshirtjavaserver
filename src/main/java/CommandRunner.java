import jssc.SerialNativeInterface;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class CommandRunner {
    String[] ports;
    SerialPort port;

    public CommandRunner() {
        ports = SerialPortList.getPortNames();
        port = new SerialPort(ports[0]);
        try {
            port.openPort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public void runCommand(Command command) {
        if (command.getCommand().equals("INVALID")) {
            System.out.println("Invalid command.");
        }

        Thread thread = new Thread(() -> {
            if (port.isOpened()) {
                try {
                    port.writeString(command.getCommand() + "(" + command.getParam() + ")");
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
