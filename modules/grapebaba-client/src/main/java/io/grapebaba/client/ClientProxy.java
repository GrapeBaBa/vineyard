package io.grapebaba.client;

import java.lang.reflect.Proxy;

import io.grapebaba.common.protocol.MessageType;
import io.grapebaba.common.protocol.SerializerType;
import io.grapebaba.common.protocol.v1.DefaultProtocolV1Header;
import io.grapebaba.common.protocol.v1.Request;
import io.grapebaba.common.protocol.v1.RequestMessage;

public class ClientProxy {

  public static <T> T create(String hostname, String port, Class<T> serviceInterface) {
    return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class[] {serviceInterface}, (proxy, method, args) -> {
			    DefaultProtocolV1Header header=DefaultProtocolV1Header.HeaderBuilder.header()
					    .withSerializerType(SerializerType.KRYO)
					    .withMessageType(MessageType.REQUEST)
					    .build();
			    Request request=Request.RequestBuilder.request()
					    .withBeanName(serviceInterface.getName())
					    .withMethodName(method.getName())
					    .withArguments(args)
					    .build();
			    RequestMessage.RequestMessageBuilder.requestMessage()
					    .withDefaultProtocolV1Header(header)
					    .withRequest(request)
					    .build();
			    return null;
		    });
  }
}
