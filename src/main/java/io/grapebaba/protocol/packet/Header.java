package io.grapebaba.protocol.packet;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Header implements io.grapebaba.protocol.Header {
    private Integer bodyLength;

    public Integer getBodyLength() {
        return bodyLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(bodyLength, header.bodyLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyLength);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bodyLength", bodyLength)
                .toString();
    }

    public static class HeaderBuilder {
        private Header header;

        private HeaderBuilder() {
            header = new Header();
        }

        public HeaderBuilder withBodyLength(Integer bodyLength) {
            header.bodyLength = bodyLength;
            return this;
        }

        public static HeaderBuilder header() {
            return new HeaderBuilder();
        }

        public Header build() {
            return header;
        }
    }
}
