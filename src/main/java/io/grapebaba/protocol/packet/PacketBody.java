package io.grapebaba.protocol.packet;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * Data packet body content.
 */
public class PacketBody implements io.grapebaba.protocol.Body {
  private ByteBuf bodyByteBuf;

  public ByteBuf getBodyByteBuf() {
    return bodyByteBuf;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    PacketBody packetBody1 = (PacketBody) obj;
    return Objects.equals(bodyByteBuf, packetBody1.bodyByteBuf);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyByteBuf);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("bodyByteBuf", bodyByteBuf).toString();
  }

  public static class BodyBuilder {
    private PacketBody packetBody;

    private BodyBuilder() {
      packetBody = new PacketBody();
    }

    public BodyBuilder withBodyByteBuf(ByteBuf bodyByteBuf) {
      packetBody.bodyByteBuf = bodyByteBuf;
      return this;
    }

    public static BodyBuilder body() {
      return new BodyBuilder();
    }

    public PacketBody build() {
      return packetBody;
    }
  }
}
