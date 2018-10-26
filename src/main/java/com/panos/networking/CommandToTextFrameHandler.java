package com.panos.networking;

import com.google.gson.Gson;
import com.panos.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class CommandToTextFrameHandler extends MessageToMessageEncoder<Command> {
    private Gson gson = new Gson();

    @Override
    protected void encode(ChannelHandlerContext ctx, Command command, List<Object> out) throws Exception {
        String json = gson.toJson(command);
        System.out.println("SENDING");
        out.add(new TextWebSocketFrame(json));
    }

}