package io.grapebaba.server;

import io.grapebaba.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public interface ServerHandler<P extends Protocol> {

    P handle(P protocol);
}
