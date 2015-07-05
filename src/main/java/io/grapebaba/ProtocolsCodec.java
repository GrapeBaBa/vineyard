package io.grapebaba;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

import io.grapebaba.annotation.ProtocolCodec;
import io.grapebaba.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class ProtocolsCodec extends MessageToMessageCodec<ByteBuf, Protocol> {
  private static final Logger logger = LoggerFactory.getLogger(ProtocolsCodec.class);

  private static final Map<Byte, ? super io.grapebaba.protocol.ProtocolCodec> registry = Maps
      .newHashMap();

  static {
    try {
      ClassPath
          .from(ProtocolsCodec.class.getClassLoader())
          .getAllClasses()
          .stream()
          .map(ClassPath.ClassInfo::load)
          .filter(clazz -> clazz.isAnnotationPresent(ProtocolCodec.class))
          .forEach(
              clazz -> {
              try {
                registry.put(clazz.getAnnotation(ProtocolCodec.class).value(),
                    (io.grapebaba.protocol.ProtocolCodec) clazz.newInstance());
              } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Register DefaultProtocolV1Codec instantiation exception", e);
                throw new RuntimeException(e);
              }
            });
    } catch (IOException e) {
      logger.error("Register DefaultProtocolV1Codec io exception", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Protocol msg, List<Object> out)
          throws Exception {

  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
          throws Exception {

  }
}
