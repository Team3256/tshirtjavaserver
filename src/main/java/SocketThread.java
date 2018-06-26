import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {
    protected Socket socket;
    protected CommandRunner runner;

    public SocketThread(Socket socket, CommandRunner runner) {
        this.socket = socket;
        this.runner = runner;
    }

    public void start() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                    runner.runCommand(new Command(line));
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("Disconnect");
                //e.printStackTrace();
                return;
            }
        }
    }
}
