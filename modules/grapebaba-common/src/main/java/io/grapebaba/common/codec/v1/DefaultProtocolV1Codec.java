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

package io.grapebaba.common.codec.v1;

import io.grapebaba.common.annotation.ProtocolCodecProvider;
import io.grapebaba.common.protocol.MessageType;
import io.grapebaba.common.protocol.v1.DefaultProtocolV1Header;
import io.grapebaba.common.protocol.ProtocolCodec;
import io.grapebaba.common.protocol.SerializerType;
import io.grapebaba.common.protocol.Version;
import io.grapebaba.common.protocol.v1.ProtocolMessage;
import io.grapebaba.common.protocol.v1.Request;
import io.grapebaba.common.protocol.v1.RequestMessage;
import io.grapebaba.common.protocol.v1.Response;
import io.grapebaba.common.protocol.v1.ResponseMessage;
import io.grapebaba.common.serializer.Serializers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.nio.charset.StandardCharsets;

import static io.grapebaba.common.protocol.MessageType.REQUEST;
import static io.grapebaba.common.protocol.MessageType.RESPONSE;

/**
 * The default rpc protocol codec.
 */
@ProtocolCodecProvider(magicNumber = ProtocolMessage.MAGIC_NUMBER)
public class DefaultProtocolV1Codec implements ProtocolCodec<ProtocolMessage> {

  @Override
  public ByteBuf encode(ProtocolMessage msg) {
    DefaultProtocolV1Header header = (DefaultProtocolV1Header) msg.header();
    switch (header.getMessageType()) {
      case REQUEST:
        return new RequestMessageCodec().encode((RequestMessage) msg);
      case RESPONSE:
        return new ResponseMessageCodec().encode((ResponseMessage) msg);
      default:
        throw new RuntimeException("Cannot decode message by DefaultProtocolV1Codec");
    }
  }

  @Override
  public ProtocolMessage decode(ByteBuf msg) {
    final int messageTypePosition = 2;
    MessageType messageType = MessageType.valueOf(msg.getByte(messageTypePosition));
    switch (messageType) {
      case REQUEST:
        return new RequestMessageCodec().decode(msg);
      case RESPONSE:
        return new ResponseMessageCodec().decode(msg);
      default:
        throw new RuntimeException("Cannot decode message by DefaultProtocolV1Codec");
    }
  }

  static final class RequestMessageCodec implements ProtocolCodec<RequestMessage> {
    @Override
    public RequestMessage decode(ByteBuf byteBuf) {
      byteBuf.readByte();
      Version.valueOf(byteBuf.readByte());
      final MessageType messageType = MessageType.valueOf(byteBuf.readByte());
      final SerializerType serializerType = SerializerType.valueOf(byteBuf.readByte());

      DefaultProtocolV1Header header =
          DefaultProtocolV1Header.HeaderBuilder.header().withMessageType(messageType)
              .withSerializerType(serializerType).build();

      final Integer opaque = byteBuf.readInt();
      final Integer timeout = byteBuf.readInt();
      final Integer beanNameLength = byteBuf.readInt();
      final String beanName = byteBuf.readBytes(beanNameLength).toString(StandardCharsets.UTF_8);
      final Integer methodNameLength = byteBuf.readInt();
      final String methodName =
          byteBuf.readBytes(methodNameLength).toString(StandardCharsets.UTF_8);
      final Integer argumentCount = byteBuf.readInt();

      final Object[] arguments = new Object[argumentCount];
      for (int i = 0; i < argumentCount - 1; i++) {
        final Integer argumentLength = byteBuf.readInt();
        final ByteBuf argument = byteBuf.readBytes(argumentLength);
        final byte[] argumentBytes = new byte[argument.readableBytes()];
        argument.readBytes(argumentBytes);
        arguments[i] = Serializers.serializer(serializerType).deserialize(argumentBytes);
      }

      final Request request =
          Request.RequestBuilder.request().withArguments(arguments).withBeanName(beanName)
              .withMethodName(methodName).withOpaque(opaque).withTimeout(timeout).build();

      return RequestMessage.RequestMessageBuilder.requestMessage()
          .withDefaultProtocolV1Header(header).withRequest(request).build();
    }

    @Override
    public ByteBuf encode(RequestMessage protocol) {
      final DefaultProtocolV1Header header = protocol.header();
      ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
      byteBuf.writeByte(protocol.getMagicNumber());
      byteBuf.writeByte(header.getVersion().getValue());
      byteBuf.writeByte(header.getMessageType().getValue());
      SerializerType serializerType = header.getSerializerType();
      byteBuf.writeByte(serializerType.getValue());

      final Request request = protocol.body();
      byteBuf.writeInt(request.getOpaque());
      byteBuf.writeInt(request.getTimeout());
      final byte[] beanName = request.getBeanName().getBytes(StandardCharsets.UTF_8);
      byteBuf.writeInt(beanName.length);
      byteBuf.writeBytes(beanName);
      final byte[] methodName = request.getMethodName().getBytes(StandardCharsets.UTF_8);
      byteBuf.writeByte(methodName.length);
      byteBuf.writeBytes(methodName);
      byteBuf.writeInt(request.getArguments().length);

      for (Object arg : request.getArguments()) {
        final byte[] argBytes = Serializers.serializer(serializerType).serialize(arg);
        byteBuf.writeInt(argBytes.length);
        byteBuf.writeBytes(argBytes);
      }

      return byteBuf;
    }

  }

  static final class ResponseMessageCodec implements ProtocolCodec<ResponseMessage> {
    @Override
    public ResponseMessage decode(ByteBuf byteBuf) {
      byteBuf.readByte();
      Version.valueOf(byteBuf.readByte());
      final MessageType messageType = MessageType.valueOf(byteBuf.readByte());
      final SerializerType serializerType = SerializerType.valueOf(byteBuf.readByte());

      DefaultProtocolV1Header header =
          DefaultProtocolV1Header.HeaderBuilder.header().withMessageType(messageType)
              .withSerializerType(serializerType).build();

      final Integer opaque = byteBuf.readInt();
      final Integer resultLength = byteBuf.readInt();
      final ByteBuf resultByteBuf = byteBuf.readBytes(resultLength);
      final byte[] resultBytes = new byte[resultByteBuf.readableBytes()];
      resultByteBuf.readBytes(resultBytes);
      final Object result = Serializers.serializer(serializerType).deserialize(resultBytes);

      final Response response =
          Response.ResponseBuilder.response().withOpaque(opaque).withResult(result).build();

      return ResponseMessage.ResponseMessageBuilder.responseMessage()
          .withDefaultProtocolV1Header(header).withResponse(response).build();
    }

    @Override
    public ByteBuf encode(ResponseMessage protocol) {
      ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
      final DefaultProtocolV1Header header = protocol.header();

      byteBuf.writeByte(protocol.getMagicNumber());
      byteBuf.writeByte(header.getVersion().getValue());
      byteBuf.writeByte(header.getMessageType().getValue());
      SerializerType serializerType = header.getSerializerType();
      byteBuf.writeByte(serializerType.getValue());

      final Response response = protocol.body();
      byteBuf.writeInt(response.getOpaque());
      final byte[] resultBytes =
          Serializers.serializer(serializerType).serialize(response.getResult());
      byteBuf.writeInt(resultBytes.length);
      byteBuf.writeBytes(resultBytes);

      return byteBuf;
    }

  }
}
