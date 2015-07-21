package io.grapebaba.codec.v1;

import io.grapebaba.annotation.ProtocolCodecProvider;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.ProtocolCodec;
import io.grapebaba.protocol.SerializerType;
import io.grapebaba.protocol.Version;
import io.grapebaba.protocol.v1.DefaultProtocolV1Header;
import io.grapebaba.protocol.v1.ProtocolMessage;
import io.grapebaba.protocol.v1.Request;
import io.grapebaba.protocol.v1.RequestMessage;
import io.grapebaba.protocol.v1.ResponseMessage;
import io.grapebaba.serializer.Serializers;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.List;

@ProtocolCodecProvider(ProtocolMessage.MAGIC_NUMBER)
public class DefaultProtocolV1Codec implements ProtocolCodec<ProtocolMessage> {
	@Override
	public void decode(ByteBuf byteBuf, List<Object> out) {

	}

	@Override
	public void encode(ProtocolMessage protocol, ByteBuf out) {

	}

	static class RequestMessageCodec implements ProtocolCodec<RequestMessage> {
		@Override
		public void decode(ByteBuf byteBuf, List<Object> out) {
			byteBuf.readByte();
			Version.valueOf(byteBuf.readByte());
			final MessageType messageType = MessageType.valueOf(byteBuf.readByte());
			final SerializerType serializerType = SerializerType.valueOf(byteBuf.readByte());

			DefaultProtocolV1Header header = DefaultProtocolV1Header.HeaderBuilder
					.header().withMessageType(messageType)
					.withSerializerType(serializerType).build();

			final Integer opaque = byteBuf.readInt();
			final Integer timeout = byteBuf.readInt();
			final Integer beanNameLength = byteBuf.readInt();
			final String beanName = byteBuf.readBytes(beanNameLength).toString(StandardCharsets.UTF_8);
			final Integer methodNameLength = byteBuf.readInt();
			final String methodName =
					byteBuf.readBytes(methodNameLength).toString(StandardCharsets.UTF_8);
			final Integer argumentCount = byteBuf.readInt();

			Object[] arguments = new Object[argumentCount];
			for (int i = 0; i < argumentCount - 1; i++) {
				final Integer argumentLength = byteBuf.readInt();
				final ByteBuf argument = byteBuf.readBytes(argumentLength);
				byte[] argumentBytes = new byte[argument.readableBytes()];
				argument.readBytes(argumentBytes);
				arguments[i] = Serializers.serializer(serializerType).deserialize(argumentBytes);
			}

			Request request=Request.RequestBuilder.request()
					.withArguments(arguments)
					.withBeanName(beanName)
					.withMethodName(methodName)
					.withOpaque(opaque)
					.withTimeout(timeout)
					.build();

			//RequestMessage message=RequestMessage.
		}

		@Override
		public void encode(RequestMessage protocol, ByteBuf out) {

		}

	}

	static class ResponseMessageCodec implements ProtocolCodec<ResponseMessage> {
		@Override
		public void decode(ByteBuf byteBuf, List<Object> out) {

		}

		@Override
		public void encode(ResponseMessage protocol, ByteBuf out) {

		}

	}
}
