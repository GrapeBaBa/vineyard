package io.grapebaba.protocol.packet;

import com.google.common.base.MoreObjects;

import io.grapebaba.protocol.Protocol;

import java.util.Objects;

/**
 * The data packet structure.
 */
public class Packet implements Protocol<PacketHeader, PacketBody> {
  private PacketHeader packetHeader;

  private PacketBody packetBody;

  @Override
  public PacketHeader header() {
    return packetHeader;
  }

  @Override
  public PacketBody body() {
    return packetBody;
  }

  @Override
  public byte getMagicNumber() {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Packet packet = (Packet) obj;
    return Objects.equals(packetHeader, packet.packetHeader)
        && Objects.equals(packetBody, packet.packetBody);
  }

  @Override
  public int hashCode() {
    return Objects.hash(packetHeader, packetBody);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("packetBody", packetBody)
        .add("packetHeader", packetHeader).toString();
  }

  public static class PacketBuilder {
    private Packet packet;

    private PacketBuilder() {
      packet = new Packet();
    }

    public PacketBuilder withPacketHeader(PacketHeader packetHeader) {
      packet.packetHeader = packetHeader;
      return this;
    }

    public PacketBuilder withPacketBody(PacketBody packetBody) {
      packet.packetBody = packetBody;
      return this;
    }

    public static PacketBuilder packet() {
      return new PacketBuilder();
    }

    public Packet build() {
      return packet;
    }
  }
}
