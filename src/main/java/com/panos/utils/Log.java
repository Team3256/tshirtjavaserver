package com.panos.utils;

import com.panos.Command;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

// Just some log functions that make it easier to debug code
public class Log {
    public static ArrayList<ChannelHandlerContext> ctxList = new ArrayList<ChannelHandlerContext>();

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
        ctxList.forEach((ctx) -> {
            Command command = new Command();

            command.command = "log";
            command.message = msg;

            ctx.writeAndFlush(command);
        });
    }

    public static void addSocketHandler(ChannelHandlerContext ctx) {
        ctxList.add(ctx);
    }

    public static void removeSocketHandle(ChannelHandlerContext ctx) {
        ctxList.remove(ctx);
    }

}
