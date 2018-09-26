package com.panos.utils;

import com.panos.Command;

// Just some log functions that make it easier to debug code
public class Log {
    private static Websocket websocket = null;

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

    public static void state(String msg) { log("STATE", msg);}

    public static void log(String level, String msg) {
        printAndSend("[" + level + "] " + msg);
    }

    public static void printAndSend(String msg) {
        System.out.println(msg);
        if (websocket != null)
            websocket.sendCommand(new Command(msg));
    }

    public static void configureWebsocket(Websocket websocket) {
        Log.websocket = websocket;
    }

}
