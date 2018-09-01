package com.panos;
import com.google.gson.Gson;
import com.panos.utils.Log;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        Log.log("MAIN", "Starting program");

        // Create new robot instance that handles low level communication
        Robot robot = new Robot();

        // Create GSON instance to decode JSON from client
        Gson gson = new Gson();

        // Create new websocket in order to listen for controller events
        WebSocketServer server = new WebSocketServer() {
            // Add listeners to messages from the websocket server
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                Log.addSocket(webSocket);
                Log.server("Client " + webSocket.getRemoteSocketAddress().getAddress().toString() + " connected");
                robot.onConnect();
            }

            // When a client disconnects, disable the robot
            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                Log.removeSocket(webSocket);
                Log.server(webSocket.getRemoteSocketAddress().getAddress().toString() + " DISCONNECTED");
                robot.emergencyStop();
            }

            // When the server gets controller input, parse the input and act upon it
            @Override
            public void onMessage(WebSocket webSocket, String s) {
                //Log.log("MAIN", s);
                // Deserialize JSON into a Java object of class Command
                Command command = gson.fromJson(s, Command.class);

                // If button pressed, then send command to the robot
                if (command.command.equals("button")) {
                    robot.onButtonPress(command.button, command.isPressed);
                }

                // If joysticks change, send that to the robot
                if (command.command.equals("axis")) {
                    robot.onAxisChange(command.lx, command.ly, command.rx, command.ry);
                }

                if (command.version == 2) {
                    Log.robot(String.valueOf(command.pivotAcceleration));
                }
            }

            // If a code error occurs, stop the robot and crash
            @Override
            public void onError(WebSocket webSocket, Exception e) {
                robot.emergencyStop();
                e.printStackTrace();
            }

            // When the
            @Override
            public void onStart() {
                Log.server("Server started successfully");
                Timer timer = new Timer();
                timer.schedule(robot, 0, 30);
            }
        };

        // Print server IP to console
        Log.server("Running server at address " + server.getAddress());

        // Start server
        server.start();
    }
}
