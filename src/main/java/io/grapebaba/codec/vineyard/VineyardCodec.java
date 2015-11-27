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

package io.grapebaba.codec.vineyard;

import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.ProtocolCodec;
import io.grapebaba.protocol.SerializerType;
import io.grapebaba.protocol.vineyard.VineyardMessage;
import io.grapebaba.protocol.vineyard.RequestMessage;
import io.grapebaba.protocol.vineyard.ResponseMessage;
import io.grapebaba.serializer.Serializers;
import io.netty.buffer.ByteBuf;

import static io.netty.buffer.PooledByteBufAllocator.DEFAULT;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Vineyard protocol codec.
 */
public class VineyardCodec implements ProtocolCodec<VineyardMessage> {

    @Override
    public VineyardMessage decode(ByteBuf byteBuf) {
        final int messageTypePosition = 0;
        MessageType messageType = MessageType.valueOf(byteBuf
                .getByte(messageTypePosition));
        switch (messageType) {
            case REQUEST:
                return new RequestMessageCodec().decode(byteBuf);
            case RESPONSE:
                return new ResponseMessageCodec().decode(byteBuf);
            default:
                throw new RuntimeException("Cannot decode message by VineyardCodec");
        }
    }

    @Override
    public ByteBuf encode(VineyardMessage message) {
        switch (message.getMessageType()) {
            case REQUEST:
                return new RequestMessageCodec().encode((RequestMessage) message);
            case RESPONSE:
                return new ResponseMessageCodec().encode((ResponseMessage) message);
            default:
                throw new RuntimeException("Cannot decode message by VineyardCodec");
        }
    }

    /**
     * Request message codec.
     */
    static final class RequestMessageCodec implements ProtocolCodec<RequestMessage> {
        @Override
        public RequestMessage decode(ByteBuf byteBuf) {
            final MessageType messageType = MessageType.valueOf(byteBuf.readByte());
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
                arguments[i] = Serializers.serializer(serializerType).deserialize(
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
                final byte[] argBytes = Serializers.serializer(serializerType).serialize(
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
            final MessageType messageType = MessageType.valueOf(byteBuf.readByte());
            final SerializerType serializerType = SerializerType.valueOf(byteBuf
                    .readByte());

            final Integer opaque = byteBuf.readInt();
            final Integer resultLength = byteBuf.readInt();
            final ByteBuf resultByteBuf = byteBuf.readBytes(resultLength);
            final byte[] resultBytes = new byte[resultByteBuf.readableBytes()];
            resultByteBuf.readBytes(resultBytes);
            final Object result = Serializers.serializer(serializerType).deserialize(
                    resultBytes);

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
            final byte[] resultBytes = Serializers.serializer(serializerType).serialize(
                    message.getResult());
            byteBuf.writeInt(resultBytes.length);
            byteBuf.writeBytes(resultBytes);

            return byteBuf;
        }

    }
}
