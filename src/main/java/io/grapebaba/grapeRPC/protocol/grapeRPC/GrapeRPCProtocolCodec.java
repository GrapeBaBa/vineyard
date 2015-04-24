package io.grapebaba.grapeRPC.protocol.grapeRPC;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class GrapeRPCProtocolCodec extends ByteToMessageCodec<GrapeRPCContext> {
    @Override
    protected void encode(ChannelHandlerContext ctx, GrapeRPCContext msg, ByteBuf out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
