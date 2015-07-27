package io.grapebaba.serializer;

/**
 * The serializer interface.
 */
public interface Serializer {
  byte[] serialize(Object object);

  Object deserialize(byte[] bytes);
}
