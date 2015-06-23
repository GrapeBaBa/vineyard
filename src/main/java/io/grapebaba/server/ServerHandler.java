package io.grapebaba.server;

import io.grapebaba.ProtocolHandler;
import io.grapebaba.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ProtocolHandler {
    private static final ServiceRegistry registry = new ServiceRegistry();

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Protocol msg) throws Exception {

    }
}
