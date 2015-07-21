package io.grapebaba.serializer;

import java.util.EnumMap;

import io.grapebaba.protocol.SerializerType;

public class Serializers {
  private static final EnumMap<SerializerType, Serializer> map =
      new EnumMap<>(SerializerType.class);

  static {
    map.put(SerializerType.FST, new FSTSerializer());
    map.put(SerializerType.KRYO, new KryoSerializer());
  }

  public static Serializer serializer(SerializerType serializerType) {
    return map.get(serializerType);
  }
}
