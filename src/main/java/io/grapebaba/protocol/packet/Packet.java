package io.grapebaba.protocol.packet;

import io.grapebaba.protocol.Protocol;

public class Packet implements Protocol<PacketHeader, PacketBody> {
  @Override
  public PacketHeader header() {
    return null;
  }

  @Override
  public PacketBody body() {
    return null;
  }

  @Override
  public byte getMagicNumber() {
    return 0;
  }
}
