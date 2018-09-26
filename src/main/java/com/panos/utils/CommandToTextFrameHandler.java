package com.panos.utils;

import com.google.gson.Gson;
import com.panos.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class CommandToTextFrameHandler extends MessageToMessageEncoder<Command> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Command command, List<Object> out) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(command);
        out.add(new TextWebSocketFrame(json));
    }

}