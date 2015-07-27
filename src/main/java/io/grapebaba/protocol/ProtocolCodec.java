package io.grapebaba.protocol;

import io.netty.buffer.ByteBuf;

/**
 * The protocol codec interface.
 * 
 * @param <P> protocol
 */
public interface ProtocolCodec<P extends Protocol> {

  P decode(ByteBuf byteBuf);

  ByteBuf encode(P protocol);
}
