package io.grapebaba.protocol;

/**
 * The default protocol message type.
 */
public enum MessageType {
  REQUEST, RESPONSE, HEARTBEAT;

  /**
   * Get byte of message type.
   * 
   * @return byte of message type
   */
  public byte getValue() {
    switch (this) {
      case REQUEST:
        return 0x00;
      case RESPONSE:
        return 0x01;
      case HEARTBEAT:
        return 0x02;
      default:
        throw new IllegalArgumentException("Unknown MessageType" + this);
    }

  }

  /**
   * Get message type.
   * 
   * @param value value
   * @return message type
   */
  public static MessageType valueOf(byte value) {
    switch (value) {
      case 0x00:
        return REQUEST;
      case 0x01:
        return RESPONSE;
      case 0x02:
        return HEARTBEAT;
      default:
        throw new IllegalArgumentException("Unknown MessageType " + value);
    }
  }
}
