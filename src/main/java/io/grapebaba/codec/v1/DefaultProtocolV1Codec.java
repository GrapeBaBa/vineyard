package io.grapebaba.codec.v1;

import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;
import io.grapebaba.protocol.Version;
import io.grapebaba.protocol.v1.DefaultProtocolV1Header;
import io.grapebaba.protocol.v1.ProtocolMessage;
import io.grapebaba.protocol.v1.RequestMessage;
import io.grapebaba.protocol.v1.ResponseMessage;
import io.netty.buffer.ByteBuf;

import java.util.List;

@io.grapebaba.annotation.ProtocolCodec(ProtocolMessage.MAGIC_NUMBER)
public class DefaultProtocolV1Codec
        implements io.grapebaba.protocol.ProtocolCodec<ProtocolMessage> {
  @Override
  public void decode(ByteBuf byteBuf, List<Object> out) {

  }

  @Override
  public void encode(ProtocolMessage protocol, ByteBuf out) {

  }

  static class RequestMessageCodec
          implements io.grapebaba.protocol.ProtocolCodec<RequestMessage> {
    @Override
    public void decode(ByteBuf byteBuf, List<Object> out) {
//      byteBuf.readByte();
//      Version.valueOf(byteBuf.readByte());
//      final MessageType messageType = MessageType.valueOf(byteBuf.readByte());
//      final SerializerType serializerType = SerializerType.valueOf(byteBuf.readByte());
//
//      DefaultProtocolV1Header.HeaderBuilder.header().withMessageType(messageType)
//              .withSerializerType(serializerType).build();
    }

    @Override
    public void encode(RequestMessage protocol, ByteBuf out) {

    }

  }

  static class ResponseMessageCodec
          implements io.grapebaba.protocol.ProtocolCodec<ResponseMessage> {
    @Override
    public void decode(ByteBuf byteBuf, List<Object> out) {

    }

    @Override
    public void encode(ResponseMessage protocol, ByteBuf out) {

    }

  }
}
