package com.panos;
import com.google.gson.Gson;
import com.panos.subsystems.RobotLocation;
import com.panos.utils.Location;
import com.panos.utils.Log;
import com.panos.utils.ShooterMath;
import com.panos.utils.Utils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.ArrayList;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        Log.log("MAIN", "Starting program");

        // Create new websocket in order to listen for controller events
        //WebSocketServer server = new Websocket();

        // Print server IP to console
        //Log.server("Running server at address " + server.getAddress());

        NioEventLoopGroup acceptorGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup handlerGroup = new NioEventLoopGroup(10);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptorGroup, handlerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new WebSocketInitializer());

        try {
            ChannelFuture ch = bootstrap.bind(8080).sync();
            ch.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            acceptorGroup.shutdownGracefully();
            handlerGroup.shutdownGracefully();
        }

        Log.server("Start on port 8080");
        // Start server
        //server.start();
    }
}
