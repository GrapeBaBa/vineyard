package io.grapebaba.server;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

import io.grapebaba.annotation.ServerHandlerProvider;
import io.grapebaba.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * The default server handler.
 */
public class ServersHandler extends SimpleChannelInboundHandler<Protocol> {
  private static final Logger logger = LoggerFactory.getLogger(ServersHandler.class);

  private static final Map<Byte, ServerHandler> HANDLER_REGISTRY = Maps.newHashMap();

  static {
    try {
      ClassPath
          .from(ServersHandler.class.getClassLoader())
          .getAllClasses()
          .stream()
          .map(ClassPath.ClassInfo::load)
          .filter(clazz -> clazz.isAnnotationPresent(ServerHandlerProvider.class))
          .forEach(
              clazz -> {
              try {
                HANDLER_REGISTRY.put(clazz.getAnnotation(ServerHandlerProvider.class)
                      .magicNumber(), (ServerHandler) clazz.newInstance());
              } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Register ServerHandler instantiation exception", e);
                throw new RuntimeException("Register ServerHandler instantiation exception", e);
              }
            });
    } catch (IOException e) {
      logger.error("Register ServerHandler io exception", e);
      throw new RuntimeException("Register ServerHandler io exception", e);
    }
  }

  @Override
  @SuppressWarnings({"unchecked"})
  protected void messageReceived(ChannelHandlerContext ctx, Protocol msg) throws Exception {
    HANDLER_REGISTRY.get(msg.getMagicNumber()).handle(msg);
  }
}
