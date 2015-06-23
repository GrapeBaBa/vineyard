package io.grapebaba.protocol.v1;

import com.google.common.base.MoreObjects;
import io.grapebaba.protocol.Version;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;

import java.util.Objects;

//TODO:add rpc way property(request/response,one-way...)
//TODO:custom protocol by set param when create client
public class Header implements io.grapebaba.protocol.Header {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(messageType, header.messageType) &&
                Objects.equals(serializerType, header.serializerType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, serializerType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageType", messageType)
                .add("serializerType", serializerType)
                .toString();
    }

    @Override
    public int length() {
        return 0;
    }

    public static class HeaderV1Builder {
        private Header header;

        private HeaderV1Builder() {
            header = new Header();
        }

        public HeaderV1Builder withMessageType(MessageType messageType) {
            header.messageType = messageType;
            return this;
        }

        public HeaderV1Builder withSerializerType(SerializerType serializerType) {
            header.serializerType = serializerType;
            return this;
        }

        public static HeaderV1Builder header() {
            return new HeaderV1Builder();
        }

        public Header build() {
            return header;
        }
    }
}
