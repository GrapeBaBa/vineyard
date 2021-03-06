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

package io.grapebaba.vineyard.grape.codec;

import io.grapebaba.vineyard.common.serializer.SerializerType;
import io.grapebaba.vineyard.grape.protocol.MessageType;
import io.grapebaba.vineyard.common.codec.ProtocolCodec;
import io.grapebaba.vineyard.grape.protocol.grape.GrapeMessage;
import io.grapebaba.vineyard.grape.protocol.grape.HeartbeatRequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.HeartbeatResponseMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.netty.buffer.ByteBuf;

import static io.grapebaba.vineyard.common.serializer.Serializers.serializer;
import static io.grapebaba.vineyard.grape.protocol.MessageType.valueOf;
import static io.netty.buffer.PooledByteBufAllocator.DEFAULT;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Grape protocol codec.
 */
public class GrapeCodec implements ProtocolCodec<GrapeMessage> {

    @Override
    public GrapeMessage decode(ByteBuf byteBuf) {
        final int messageTypePosition = 0;
        MessageType messageType = valueOf(byteBuf
                .getByte(messageTypePosition));
        switch (messageType) {
            case REQUEST:
                return new RequestMessageCodec().decode(byteBuf);
            case RESPONSE:
                return new ResponseMessageCodec().decode(byteBuf);
            case HEARTBEAT_REQUEST:
                return new HeartbeatRequestMessage();
            case HEARTBEAT_RESPONSE:
                return new HeartbeatResponseMessage();
            default:
                throw new RuntimeException("Cannot decode message by GrapeCodec");
        }
    }

    @Override
    public ByteBuf encode(GrapeMessage message) {
        switch (message.getMessageType()) {
            case REQUEST:
                return new RequestMessageCodec().encode((RequestMessage) message);
            case RESPONSE:
                return new ResponseMessageCodec().encode((ResponseMessage) message);
            case HEARTBEAT_REQUEST:
                return getHeartbeatByteBuf(message);
            case HEARTBEAT_RESPONSE:
                return getHeartbeatByteBuf(message);
            default:
                throw new RuntimeException("Cannot decode message by GrapeCodec");
        }
    }

    /**
     * Request message codec.
     */
    static final class RequestMessageCodec implements ProtocolCodec<RequestMessage> {
        @Override
        public RequestMessage decode(ByteBuf byteBuf) {
            final MessageType messageType = valueOf(byteBuf.readByte());
            final SerializerType serializerType = SerializerType.valueOf(byteBuf
                    .readByte());

            final Integer opaque = byteBuf.readInt();
            final Integer timeout = byteBuf.readInt();
            final Integer beanNameLength = byteBuf.readInt();
            final String beanName = byteBuf.readBytes(beanNameLength).toString(UTF_8);
            final Integer methodNameLength = byteBuf.readInt();
            final String methodName = byteBuf.readBytes(methodNameLength).toString(UTF_8);
            final Integer argumentCount = byteBuf.readInt();

            final Object[] arguments = new Object[argumentCount];
            for (int i = 0; i < argumentCount; i++) {
                final Integer argumentLength = byteBuf.readInt();
                final ByteBuf argument = byteBuf.readBytes(argumentLength);
                final byte[] argumentBytes = new byte[argument.readableBytes()];
                argument.readBytes(argumentBytes);
                arguments[i] = serializer(serializerType).deserialize(
                        argumentBytes);
            }

            return RequestMessage.newBuilder().withMessageType(messageType)
                    .withSerializerType(serializerType).withOpaque(opaque)
                    .withTimeout(timeout).withBeanName(beanName)
                    .withMethodName(methodName).withArguments(arguments).build();
        }

        @Override
        public ByteBuf encode(RequestMessage message) {
            ByteBuf byteBuf = DEFAULT.buffer();
            byteBuf.writeByte(message.getMessageType().getValue());
            SerializerType serializerType = message.getSerializerType();
            byteBuf.writeByte(serializerType.getValue());

            byteBuf.writeInt(message.getOpaque());
            byteBuf.writeInt(message.getTimeout());
            final byte[] beanName = message.getBeanName().getBytes(UTF_8);
            byteBuf.writeInt(beanName.length);
            byteBuf.writeBytes(beanName);
            final byte[] methodName = message.getMethodName().getBytes(UTF_8);
            byteBuf.writeInt(methodName.length);
            byteBuf.writeBytes(methodName);
            byteBuf.writeInt(message.getArguments().length);

            for (Object arg : message.getArguments()) {
                final byte[] argBytes = serializer(serializerType).serialize(
                        arg);
                byteBuf.writeInt(argBytes.length);
                byteBuf.writeBytes(argBytes);
            }

            return byteBuf;
        }

    }

    /**
     * Response message codec.
     */
    static final class ResponseMessageCodec implements ProtocolCodec<ResponseMessage> {
        @Override
        public ResponseMessage decode(ByteBuf byteBuf) {
            final MessageType messageType = valueOf(byteBuf.readByte());
            final SerializerType serializerType = SerializerType.valueOf(byteBuf
                    .readByte());

            final Integer opaque = byteBuf.readInt();
            final Integer resultLength = byteBuf.readInt();
            final Object result;
            if (resultLength > 0) {
                final ByteBuf resultByteBuf = byteBuf.readBytes(resultLength);
                final byte[] resultBytes = new byte[resultByteBuf.readableBytes()];
                resultByteBuf.readBytes(resultBytes);
                result = serializer(serializerType).deserialize(
                        resultBytes);
            } else {
                result = null;
            }

            return ResponseMessage.newBuilder().withMessageType(messageType)
                    .withSerializerType(serializerType).withOpaque(opaque)
                    .withResult(result).build();
        }

        @Override
        public ByteBuf encode(ResponseMessage message) {
            ByteBuf byteBuf = DEFAULT.buffer();

            byteBuf.writeByte(message.getMessageType().getValue());
            SerializerType serializerType = message.getSerializerType();
            byteBuf.writeByte(serializerType.getValue());

            byteBuf.writeInt(message.getOpaque());
            Object result = message.getResult();
            if (null != result) {
                final byte[] resultBytes = serializer(serializerType).serialize(
                        message.getResult());
                byteBuf.writeInt(resultBytes.length);
                byteBuf.writeBytes(resultBytes);
            } else {
                byteBuf.writeInt(0);
            }

            return byteBuf;
        }

    }

    private ByteBuf getHeartbeatByteBuf(GrapeMessage message) {
        ByteBuf resByteBuf = DEFAULT.buffer();
        resByteBuf.writeByte(message.getMessageType().getValue());
        return resByteBuf;
    }
}
