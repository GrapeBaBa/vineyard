package io.grapebaba.codec.v1;

import io.grapebaba.protocol.Protocol;
import io.grapebaba.protocol.v1.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class ProtocolCodec extends ByteToMessageCodec<Protocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Protocol msg, ByteBuf out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }

    class RequestMessageCodec extends ByteToMessageCodec<RequestMessage> {

        @Override
        protected void encode(ChannelHandlerContext ctx, RequestMessage msg, ByteBuf out) throws Exception {

        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        }
    }

    class ResponseMessageCodec extends ByteToMessageCodec<ResponseMessageCodec> {

        @Override
        protected void encode(ChannelHandlerContext ctx, ResponseMessageCodec msg, ByteBuf out) throws Exception {

        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        }
    }
}
