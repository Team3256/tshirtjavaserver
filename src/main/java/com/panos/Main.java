package com.panos;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        Robot robot = new Robot();
        Gson gson = new Gson();
        WebSocketServer server = new WebSocketServer() {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                System.out.println("CONNECTED");
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                robot.emergencyStop();
            }

            @Override
            public void onMessage(WebSocket webSocket, String s) {
                Command command = gson.fromJson(s, Command.class);
                if (command.command.equals("button")) {
                    robot.onButtonPress(command.button, command.isPressed);
                }

                if (command.command.equals("axis")) {
                    robot.onAxisChange(command.lx, command.ly, command.rx, command.ry);
                }
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                robot.emergencyStop();
                e.printStackTrace();
            }

            @Override
            public void onStart() {
                System.out.println("START");
            }
        };
        System.out.println(server.getAddress());
        //server.start();
        server.run();
    }
}
