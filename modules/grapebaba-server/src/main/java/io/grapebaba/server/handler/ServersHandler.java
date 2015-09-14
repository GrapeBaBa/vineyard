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

package io.grapebaba.server.handler;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;

import io.grapebaba.server.annotation.ServerHandlerProvider;
import io.grapebaba.common.config.Configuration;
import io.grapebaba.server.config.ServerConfiguration;
import io.grapebaba.common.protocol.Protocol;
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
