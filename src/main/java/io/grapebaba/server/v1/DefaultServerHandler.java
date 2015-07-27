package io.grapebaba.server.v1;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

import io.grapebaba.annotation.ServerHandlerProvider;
import io.grapebaba.annotation.Service;
import io.grapebaba.annotation.ServiceInterface;
import io.grapebaba.config.ServerConfiguration;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.v1.DefaultProtocolV1Header;
import io.grapebaba.protocol.v1.ProtocolMessage;
import io.grapebaba.protocol.v1.Request;
import io.grapebaba.protocol.v1.RequestMessage;
import io.grapebaba.protocol.v1.Response;
import io.grapebaba.protocol.v1.ResponseMessage;
import io.grapebaba.server.ServerHandler;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * The default rpc server handler.
 */
@ServerHandlerProvider(magicNumber = ProtocolMessage.MAGIC_NUMBER)
public class DefaultServerHandler implements ServerHandler<ProtocolMessage> {
  private static final Logger logger = LoggerFactory.getLogger(DefaultServerHandler.class);

  private final Map<String, Object> serviceRegistry = Maps.newHashMap();

  /**
   * Register services.
   * 
   * @param packageName packageName
   */
  @Override
  public void registerServices(String packageName) {
    try {
      ClassPath
          .from(DefaultServerHandler.class.getClassLoader())
          .getTopLevelClassesRecursive(packageName)
          .stream()
          .map(ClassPath.ClassInfo::load)
          .filter(clazz -> clazz.isAnnotationPresent(Service.class))
          .forEach(
                  clazz -> Arrays.stream(clazz.getInterfaces())
                          .filter(aClass -> aClass.isAnnotationPresent(ServiceInterface.class))
                          .forEach(aClass -> {
                              try {
                                serviceRegistry.put(aClass.getName(), clazz.newInstance());
                              } catch (InstantiationException | IllegalAccessException e) {
                                logger.error("Register ServerHandler reflection exception", e);
                                throw new RuntimeException("Register ServerHandler io exception",
                                         e);
                              }
                            }));
    } catch (IOException e) {
      logger.error("Register ServerHandler io exception", e);
      throw new RuntimeException("Register ServerHandler io exception", e);
    }
  }

  @Override
  public ProtocolMessage handle(ProtocolMessage protocol) {
    final RequestMessage requestMessage = (RequestMessage) protocol;
    final Request request = requestMessage.body();

    Object result;
    try {
      result =
          MethodUtils.invokeMethod(serviceRegistry.get(request.getBeanName()),
              request.getMethodName(), request.getArguments());
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      logger.error("Invoke service method exception", e);
      result = new RuntimeException("Invoke service method exception", e);
    }

    DefaultProtocolV1Header header =
        DefaultProtocolV1Header.HeaderBuilder.header()
            .withSerializerType(requestMessage.header().getSerializerType())
            .withMessageType(MessageType.RESPONSE).build();

    Response response =
        Response.ResponseBuilder.response().withOpaque(request.getOpaque()).withResult(result)
            .build();
    return ResponseMessage.ResponseMessageBuilder.responseMessage()
        .withDefaultProtocolV1Header(header).withResponse(response).build();
  }
}
