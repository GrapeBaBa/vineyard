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

import com.google.common.base.MoreObjects;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;

import java.util.Objects;

//TODO: add rpc way property(request/response,one-way...)
//TODO: custom protocol by set param when create common

/**
 * The default rpc protocol request message.
 */
public final class RequestMessage implements VineyardMessage {

    private MessageType messageType;

    private SerializerType serializerType;

    private Integer timeout;

    private String beanName;

    private String methodName;

    private Object[] arguments;

    private Integer opaque;

    private RequestMessage(Builder builder) {
        messageType = builder.messageType;
        serializerType = builder.serializerType;
        opaque = builder.opaque;
        timeout = builder.timeout;
        beanName = builder.beanName;
        methodName = builder.methodName;
        arguments = builder.arguments;
    }

    /**
     * Get a builder object.
     *
     * @return builder object
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

    public Integer getTimeout() {
        return timeout;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestMessage that = (RequestMessage) o;
        return Objects.equals(messageType, that.messageType)
                && Objects.equals(serializerType, that.serializerType)
                && Objects.equals(opaque, that.opaque)
                && Objects.equals(timeout, that.timeout)
                && Objects.equals(beanName, that.beanName)
                && Objects.equals(methodName, that.methodName)
                && Objects.deepEquals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, serializerType, opaque, timeout, beanName,
                methodName, arguments);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("messageType", messageType)
                .add("serializerType", serializerType).add("opaque", opaque)
                .add("timeout", timeout).add("beanName", beanName)
                .add("methodName", methodName).add("arguments", arguments).toString();
    }

    /**
     * A request message builder object.
     */
    public static final class Builder {
        private MessageType messageType;
        private SerializerType serializerType;
        private Integer opaque;
        private Integer timeout;
        private String beanName;
        private String methodName;
        private Object[] arguments;

        private Builder() {
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
         * Set timeout.
         *
         * @param timeout input
         * @return builder object
         */
        public Builder withTimeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Set beanName.
         *
         * @param beanName input
         * @return builder object
         */
        public Builder withBeanName(String beanName) {
            this.beanName = beanName;
            return this;
        }

        /**
         * Set methodName.
         *
         * @param methodName input
         * @return builder object
         */
        public Builder withMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        /**
         * Set arguments.
         *
         * @param arguments input
         * @return builder object
         */
        public Builder withArguments(Object[] arguments) {
            this.arguments = arguments;
            return this;
        }

        /**
         * Create builder object.
         *
         * @return builder object.
         */
        public RequestMessage build() {
            return new RequestMessage(this);
        }
    }


}
