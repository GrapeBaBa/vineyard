package io.grapebaba.codec.packet;

import io.grapebaba.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Decode the data packet through content length.
 */
public class PacketDecoder extends ReplayingDecoder<Packet> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    out.add(in.readBytes(in.readInt()));
  }
}
