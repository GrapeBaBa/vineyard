package io.grapebaba.protocol;

import io.netty.buffer.ByteBuf;

public interface ProtocolCodec<P extends Protocol> {

    P decode(ByteBuf byteBuf);

    ByteBuf encode(P protocol);
}
