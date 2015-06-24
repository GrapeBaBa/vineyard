package io.grapebaba.protocol.packet;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

public class Body implements io.grapebaba.protocol.Body {
    private ByteBuf bodyByteBuf;

    public ByteBuf getBodyByteBuf() {
        return bodyByteBuf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Body body1 = (Body) o;
        return Objects.equals(bodyByteBuf, body1.bodyByteBuf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyByteBuf);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bodyByteBuf", bodyByteBuf)
                .toString();
    }

    public static class BodyBuilder {
        private Body body;

        private BodyBuilder() {
            body = new Body();
        }

        public BodyBuilder withBodyByteBuf(ByteBuf bodyByteBuf) {
            body.bodyByteBuf = bodyByteBuf;
            return this;
        }

        public static BodyBuilder body() {
            return new BodyBuilder();
        }

        public Body build() {
            return body;
        }
    }
}
