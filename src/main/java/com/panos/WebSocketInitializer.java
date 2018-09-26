package com.panos;

import com.panos.utils.CommandToTextFrameHandler;
import com.panos.utils.SocketHandler;
import com.panos.utils.TextFrameToCommandHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.java_websocket.server.WebSocketServer;

public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        p.addLast("encoder", new HttpResponseEncoder());
        p.addLast("decoder", new HttpRequestDecoder());
        p.addLast("aggregator", new HttpObjectAggregator(65536));
        p.addLast(new CommandToTextFrameHandler());
        p.addLast(new TextFrameToCommandHandler());
        p.addLast("handler", new SocketHandler());
    }
}