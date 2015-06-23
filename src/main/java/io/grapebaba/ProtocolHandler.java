package io.grapebaba;

import io.grapebaba.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class ProtocolHandler extends SimpleChannelInboundHandler<Protocol> {


}
