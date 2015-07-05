package io.grapebaba.protocol;

public enum Version {
  V1;

  /**
   * Get byte of version.
   * 
   * @return byte of version
   */
  public byte getValue() {
    switch (this) {
      case V1:
        return 0x00;
      default:
        throw new IllegalArgumentException("Unknown Version" + this);
    }

  }

  /**
   * Get version.
   * 
   * @param value value
   * @return version
   */
  public static Version valueOf(byte value) {
    switch (value) {
      case 0x00:
        return V1;
      default:
        throw new IllegalArgumentException("Unknown Version " + value);
    }
  }
}
