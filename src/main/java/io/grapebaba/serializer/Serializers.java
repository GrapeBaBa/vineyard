package io.grapebaba.serializer;

import io.grapebaba.protocol.SerializerType;

import java.util.EnumMap;


/**
 * The serializer factory.
 */
public class Serializers {
  private static final EnumMap<SerializerType, Serializer> map =
      new EnumMap<>(SerializerType.class);

  static {
    map.put(SerializerType.FST, new FastSerializer());
    map.put(SerializerType.KRYO, new KryoSerializer());
  }

  public static Serializer serializer(SerializerType serializerType) {
    return map.get(serializerType);
  }
}
