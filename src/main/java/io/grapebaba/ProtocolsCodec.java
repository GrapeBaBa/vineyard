package io.grapebaba;

import com.google.common.collect.Maps;
import io.grapebaba.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;
import java.util.Map;

public class ProtocolsCodec extends ByteToMessageCodec<Protocol> {
    private static final Map<Byte, ? extends ByteToMessageCodec<Protocol>> registry = Maps.newHashMap();

    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol msg, ByteBuf out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
