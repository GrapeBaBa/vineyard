package io.grapebaba.server;

import com.google.common.collect.Maps;
import io.grapebaba.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

public class ServersHandler extends SimpleChannelInboundHandler<Protocol> {
    private static final Map<Byte, ? super ServerHandler> registry = Maps.newHashMap();

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Protocol msg) throws Exception {

    }
}
