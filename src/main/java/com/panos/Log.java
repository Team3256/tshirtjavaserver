package com.panos;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;

import java.util.ArrayList;

// Just some log functions that make it easier to debug code
public class Log {
    private static ArrayList<WebSocket> sockets = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void server(String msg) {
        printAndSend("[SERVER] " + msg);
    }

    public static void arduino(String msg) {
        printAndSend("[ARDUINO] " + msg);
    }

    public static void robot(String msg) {
        printAndSend("[ROBOT] " + msg);
    }

    public static void serial(String msg) {
        printAndSend("[SERIAL] " + msg);
    }

    public static void log(String level, String msg) {
        printAndSend("[" + level + "] " + msg);
    }

    public static void printAndSend(String msg) {
        Command command = new Command(msg);
        for (int i = 0; i < sockets.size(); i++) {
            sockets.get(i).send(gson.toJson(command));
        }
        System.out.println(msg);
    }

    public static void addSocket(WebSocket socket) {
        sockets.add(socket);
    }

    public static void removeSocket(WebSocket socket) {
        sockets.remove(socket);
    }
}
