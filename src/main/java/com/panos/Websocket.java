package com.panos;

import com.google.gson.Gson;
import com.panos.utils.Log;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.ArrayList;
import java.util.Timer;

public class Websocket extends WebSocketServer {
    private ArrayList<WebSocket> sockets = new ArrayList<>();
    private Gson gson = new Gson();
    private Robot robot = Robot.getInstance();

    // Add listeners to messages from the websocket server
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        sockets.add(webSocket);
        Log.server("Client " + webSocket.getRemoteSocketAddress().getAddress().toString() + " connected");
        robot.onConnect();
    }

    // When a client disconnects, disable the robot
    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        sockets.remove(webSocket);
        Log.server(webSocket.getRemoteSocketAddress().getAddress().toString() + " DISCONNECTED");
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

        if (command.command.equals("ping")) {
            this.sendCommand(new Command(command.startMs, System.currentTimeMillis()));
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
        Log.arduino("Waiting 120 seconds for Arduino to finish starting up");
        //Utils.delay(120 * 1000);
        Timer timer = new Timer();
        timer.schedule(robot, 0, 5);
        Log.configureWebsocket(this);
    }

    public void sendCommand(Command command) {
        for (int i = 0; i < sockets.size(); i++) {
            sockets.get(i).send(gson.toJson(command));
        }
    }
}
