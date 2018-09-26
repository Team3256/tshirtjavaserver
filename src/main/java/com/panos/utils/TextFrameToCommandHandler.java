package com.panos.utils;

import com.google.gson.Gson;
import com.panos.Command;
import com.panos.Robot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class TextFrameToCommandHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private Gson gson = new Gson();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        Command command = gson.fromJson(frame.text(), Command.class);

        if (command.command.equals("button")) {
            Robot.getInstance().onButtonPress(command.button, command.isPressed);
        }

        // If joysticks change, send that to the robot
        if (command.command.equals("axis")) {
            Robot.getInstance().onAxisChange(command.lx, command.ly, command.rx, command.ry);
        }

        if (command.command.equals("ping")) {
            ctx.writeAndFlush(new Command(command.startMs, System.currentTimeMillis()));
        }

        ctx.fireChannelRead(command);
    }
}
