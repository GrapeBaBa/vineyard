package io.grapebaba;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;

import io.grapebaba.annotation.ProtocolCodecProvider;
import io.grapebaba.protocol.Protocol;
import io.grapebaba.protocol.ProtocolCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The default protocol codec.
 */
public class ProtocolsCodec extends MessageToMessageCodec<ByteBuf, Protocol> {
  private static final Logger logger = LoggerFactory.getLogger(ProtocolsCodec.class);

  private static final Map<Byte, ProtocolCodec> CODEC_REGISTRY = Maps.newHashMap();

  static {
    try {
      ClassPath
          .from(ProtocolsCodec.class.getClassLoader())
          .getAllClasses()
          .stream()
          .map(ClassPath.ClassInfo::load)
          .filter(clazz -> clazz.isAnnotationPresent(ProtocolCodecProvider.class))
          .forEach(
              clazz -> {
              try {
                CODEC_REGISTRY.put(clazz.getAnnotation(ProtocolCodecProvider.class).magicNumber(),
                    (ProtocolCodec) clazz.newInstance());
              } catch (InstantiationException | IllegalAccessException e) {
                logger.error("Register codec instantiation exception", e);
                throw new RuntimeException("Register codec instantiation exception", e);
              }
            });
    } catch (IOException e) {
      logger.error("Register codec io exception", e);
      throw new RuntimeException("Register codec io exception", e);
    }
  }

  @Override
  @SuppressWarnings({"unchecked"})
  protected void encode(ChannelHandlerContext ctx, Protocol msg, List<Object> out)
          throws Exception {
    out.add(CODEC_REGISTRY.get(msg.getMagicNumber()).encode(msg));
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    final int magicNumberPosition = 0;
    final byte magicNumber = msg.getByte(magicNumberPosition);
    out.add(CODEC_REGISTRY.get(magicNumber).decode(msg));
  }
}
