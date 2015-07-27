package io.grapebaba.codec.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Encode message to data packet with content length.
 */
public class PacketEncoder extends MessageToByteEncoder<ByteBuf> {
  @Override
  protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
    if (!msg.isReadable()) {
      return;
    }

    out.writeInt(msg.readableBytes()).writeBytes(msg);
  }
}
