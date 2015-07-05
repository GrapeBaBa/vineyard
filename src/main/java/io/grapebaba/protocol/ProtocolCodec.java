package io.grapebaba.protocol;

import io.netty.buffer.ByteBuf;

import java.util.List;


public interface ProtocolCodec<P extends Protocol> {

  void decode(ByteBuf byteBuf, List<Object> out);

  void encode(P protocol, ByteBuf out);
}
