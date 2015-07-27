package io.grapebaba.protocol.packet;

import static com.google.common.base.MoreObjects.toStringHelper;

import io.grapebaba.protocol.Header;

import java.util.Objects;

/**
 * Data packet header structure.
 */
public class PacketHeader implements Header {
  private Integer bodyLength;

  public Integer getBodyLength() {
    return bodyLength;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    PacketHeader packetHeader = (PacketHeader) obj;
    return Objects.equals(bodyLength, packetHeader.bodyLength);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyLength);
  }

  @Override
  public String toString() {
    return toStringHelper(this).add("bodyLength", bodyLength).toString();
  }

  public static final class HeaderBuilder {
    private PacketHeader packetHeader;

    private HeaderBuilder() {
      packetHeader = new PacketHeader();
    }

    public HeaderBuilder withBodyLength(Integer bodyLength) {
      packetHeader.bodyLength = bodyLength;
      return this;
    }

    public static HeaderBuilder header() {
      return new HeaderBuilder();
    }

    public PacketHeader build() {
      return packetHeader;
    }
  }
}
