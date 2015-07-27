package io.grapebaba.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;

import io.grapebaba.annotation.ServerHandlerProvider;
import io.grapebaba.config.Configuration;
import io.grapebaba.config.ServerConfiguration;
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

  private final Map<Byte, ServerHandler> handlerRegistry = Maps.newHashMap();

  /**
   * Construct servers handler through server configuration.
   * 
   * @param serverConfiguration serverConfiguration
   */
  public ServersHandler(ServerConfiguration serverConfiguration) {
    try {
      ImmutableSet<ClassPath.ClassInfo> internalServerHandlers =
          ClassPath.from(ServersHandler.class.getClassLoader()).getTopLevelClassesRecursive(
              Configuration.INTERNAL_PACKAGE);

      ImmutableSet<ClassPath.ClassInfo> customServerHandlers =
          serverConfiguration.getServerHandlerPackage().isPresent() ? ClassPath.from(
              ServersHandler.class.getClassLoader()).getTopLevelClassesRecursive(
              serverConfiguration.getServerHandlerPackage().get()) : ImmutableSet
              .<ClassPath.ClassInfo>of();

      Sets.union(internalServerHandlers, customServerHandlers)
          .stream()
          .map(ClassPath.ClassInfo::load)
          .filter(clazz -> clazz.isAnnotationPresent(ServerHandlerProvider.class))
          .forEach(
              clazz -> {
              try {
                ServerHandler serverHandler = (ServerHandler) clazz.newInstance();
                serverHandler.registerServices(
                    serverConfiguration.getServicePackage().orElseThrow(() ->
                    new RuntimeException("Must set package name for service registry")));
                handlerRegistry.put(clazz.getAnnotation(ServerHandlerProvider.class)
                      .magicNumber(), serverHandler);
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
    handlerRegistry.get(msg.getMagicNumber()).handle(msg);
  }
}
