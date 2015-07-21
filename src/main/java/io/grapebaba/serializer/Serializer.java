package io.grapebaba.serializer;

public interface Serializer {
  byte[] serialize(Object object);

  Object deserialize(byte[] bytes);
}
