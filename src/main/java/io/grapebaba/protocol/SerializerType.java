package io.grapebaba.protocol;

/**
 * The default serializer type.
 */
public enum SerializerType {
  FST, KRYO;

  /**
   * Get byte of serializer type.
   * 
   * @return byte of serializer type
   */
  public byte getValue() {
    switch (this) {
      case FST:
        return 0x00;
      case KRYO:
        return 0x01;
      default:
        throw new IllegalArgumentException("Unknown SerializerType" + this);
    }

  }

  /**
   * Get serializer type.
   * 
   * @param value value
   * @return serializer type
   */
  public static SerializerType valueOf(byte value) {
    switch (value) {
      case 0x00:
        return FST;
      case 0x01:
        return KRYO;
      default:
        throw new IllegalArgumentException("Unknown SerializerType" + value);
    }
  }
}
