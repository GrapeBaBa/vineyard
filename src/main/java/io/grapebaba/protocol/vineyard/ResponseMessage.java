/*
 * Copyright 2015 281165273grape@gmail.com
 *
 * Licensed under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.grapebaba.protocol.vineyard;

import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * The default rpc protocol response message.
 */
public final class ResponseMessage implements VineyardMessage {
    private MessageType messageType;

    private SerializerType serializerType;

    private Object result;

    private Integer opaque;

    private ResponseMessage(Builder builder) {
        opaque = builder.opaque;
        messageType = builder.messageType;
        serializerType = builder.serializerType;
        result = builder.result;
    }

    /**
     * Create a builder object.
     *
     * @return builder object.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public Integer getOpaque() {
        return opaque;
    }

    public SerializerType getSerializerType() {
        return serializerType;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseMessage that = (ResponseMessage) o;
        return Objects.equals(messageType, that.messageType)
                && Objects.equals(serializerType, that.serializerType)
                && Objects.equals(opaque, that.opaque)
                && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, serializerType, opaque, result);
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("messageType", messageType)
                .add("serializerType", serializerType).add("opaque", opaque)
                .add("result", result).toString();
    }

    /**
     * A response message builder object.
     */
    public static final class Builder {
        private Integer opaque;
        private MessageType messageType;
        private SerializerType serializerType;
        private Object result;

        private Builder() {
        }

        /**
         * Set opaque.
         *
         * @param opaque input
         * @return builder object
         */
        public Builder withOpaque(Integer opaque) {
            this.opaque = opaque;
            return this;
        }

        /**
         * Set message type.
         *
         * @param messageType input
         * @return builder object
         */
        public Builder withMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        /**
         * Set serializer type.
         *
         * @param serializerType input
         * @return builder object
         */
        public Builder withSerializerType(SerializerType serializerType) {
            this.serializerType = serializerType;
            return this;
        }

        /**
         * Set result.
         *
         * @param result input
         * @return builder object
         */
        public Builder withResult(Object result) {
            this.result = result;
            return this;
        }

        /**
         * Create builder object.
         *
         * @return builder object.
         */
        public ResponseMessage build() {
            return new ResponseMessage(this);
        }
    }
}
