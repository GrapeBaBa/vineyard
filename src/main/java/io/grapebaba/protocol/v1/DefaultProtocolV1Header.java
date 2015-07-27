package io.grapebaba.protocol.v1;

import com.google.common.base.MoreObjects;

import io.grapebaba.protocol.Header;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;
import io.grapebaba.protocol.Version;

import java.util.Objects;

// TODO:add rpc way property(request/response,one-way...)
// TODO:custom protocol by set param when create client

/**
 * Default rpc protocol header.
 */
public class DefaultProtocolV1Header implements Header {

  private MessageType messageType;

  private SerializerType serializerType;

  public Version getVersion() {
    return Version.V1;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public SerializerType getSerializerType() {
    return serializerType;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    DefaultProtocolV1Header defaultProtocolV1Header = (DefaultProtocolV1Header) obj;
    return Objects.equals(messageType, defaultProtocolV1Header.messageType)
        && Objects.equals(serializerType, defaultProtocolV1Header.serializerType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageType, serializerType);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("messageType", messageType)
        .add("serializerType", serializerType).toString();
  }

  public static final class HeaderBuilder {
    private DefaultProtocolV1Header defaultProtocolV1Header;

    private HeaderBuilder() {
      defaultProtocolV1Header = new DefaultProtocolV1Header();
    }

    public HeaderBuilder withMessageType(MessageType messageType) {
      defaultProtocolV1Header.messageType = messageType;
      return this;
    }

    public HeaderBuilder withSerializerType(SerializerType serializerType) {
      defaultProtocolV1Header.serializerType = serializerType;
      return this;
    }

    public static HeaderBuilder header() {
      return new HeaderBuilder();
    }

    public DefaultProtocolV1Header build() {
      return defaultProtocolV1Header;
    }
  }
}
