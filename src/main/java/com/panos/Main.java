package com.panos;
import com.google.gson.Gson;
import com.panos.subsystems.RobotLocation;
import com.panos.utils.Location;
import com.panos.utils.Log;
import com.panos.utils.ShooterMath;
import com.panos.utils.Utils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.ArrayList;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        Log.log("MAIN", "Starting program");

        // Create new websocket in order to listen for controller events
        WebSocketServer server = new Websocket();

        // Print server IP to console
        Log.server("Running server at address " + server.getAddress());

        // Start server
        server.start();
    }
}
