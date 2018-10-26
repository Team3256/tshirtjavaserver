package com.panos;
import com.panos.networking.WebSocketInitializer;
import com.panos.utils.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Main {
    public static void main(String[] args) {
        Log.log("MAIN", "Starting program");

        Robot.getInstance();

        NioEventLoopGroup acceptorGroup = new NioEventLoopGroup(2);
        NioEventLoopGroup handlerGroup = new NioEventLoopGroup(10);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptorGroup, handlerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketInitializer());

        try {
            Log.server("Starting on port 8080");
            ChannelFuture ch = bootstrap.bind(8080).sync();
            ch.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            acceptorGroup.shutdownGracefully();
            handlerGroup.shutdownGracefully();
        }
    }
}
