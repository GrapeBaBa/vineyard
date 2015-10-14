package io.grapebaba.client;

import com.typesafe.config.Config;

import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import io.grapebaba.client.config.ClientConfiguration;
import io.grapebaba.common.protocol.MessageType;
import io.grapebaba.common.protocol.SerializerType;
import io.grapebaba.common.protocol.v1.DefaultProtocolV1Header;
import io.grapebaba.common.protocol.v1.Request;
import io.grapebaba.common.protocol.v1.RequestMessage;
import io.grapebaba.common.protocol.v1.ResponseMessage;
import io.netty.util.AttributeKey;

public class DefaultClient implements Function<RequestMessage, CompletableFuture<ResponseMessage>> {
  @Override
  public CompletableFuture<ResponseMessage> apply(RequestMessage requestMessage) {
    return null;
  }

  public static <T> T cast(String hostname, String port, Class<T> serviceInterface,
      ClientConfiguration clientConfiguration) {
	  AttributeKey.valueOf()
    return (T) Proxy.newProxyInstance(
        Thread.currentThread().getContextClassLoader(),
        new Class[] {serviceInterface},
        (proxy, method, args) -> {
          DefaultProtocolV1Header header =
              DefaultProtocolV1Header.HeaderBuilder.header()
                  .withSerializerType(SerializerType.KRYO).withMessageType(MessageType.REQUEST)
                  .build();
          Request request =
              Request.RequestBuilder.request().withBeanName(serviceInterface.getName())
                  .withMethodName(method.getName()).withArguments(args).build();
          RequestMessage.RequestMessageBuilder.requestMessage().withDefaultProtocolV1Header(header)
              .withRequest(request).build();
          return null;
        });
  }
}
