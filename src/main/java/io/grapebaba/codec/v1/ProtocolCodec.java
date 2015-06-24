package io.grapebaba.codec.v1;

import io.grapebaba.protocol.v1.ProtocolMessage;
import io.grapebaba.protocol.v1.RequestMessage;
import io.grapebaba.protocol.v1.ResponseMessage;
import io.netty.buffer.ByteBuf;

@io.grapebaba.annotation.ProtocolCodec(ProtocolMessage.MAGIC_NUMBER)
public class ProtocolCodec implements io.grapebaba.protocol.ProtocolCodec<ProtocolMessage> {

    @Override
    public ProtocolMessage decode(ByteBuf byteBuf) {
        return null;
    }

    @Override
    public ByteBuf encode(ProtocolMessage protocol) {
        return null;
    }

    class RequestMessageCodec implements io.grapebaba.protocol.ProtocolCodec<RequestMessage> {

        @Override
        public RequestMessage decode(ByteBuf byteBuf) {
            return null;
        }

        @Override
        public ByteBuf encode(RequestMessage protocol) {
            return null;
        }
    }

    class ResponseMessageCodec implements io.grapebaba.protocol.ProtocolCodec<ResponseMessage> {

        @Override
        public ResponseMessage decode(ByteBuf byteBuf) {
            return null;
        }

        @Override
        public ByteBuf encode(ResponseMessage protocol) {
            return null;
        }
    }
}
