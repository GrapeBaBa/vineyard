package io.grapebaba.grapeRPC.protocol.grapeRPC;

public enum MessageType {
    REQUEST,
    RESPONSE,
    HEARTBEAT;

    public byte getValue() {
        switch (this) {
            case REQUEST:
                return 0x00;
            case RESPONSE:
                return 0x01;
            case HEARTBEAT:
                return 0x02;
        }

        throw new IllegalArgumentException("Unknown MessageType" + this);
    }


    public static MessageType valueOf(byte value) {
        switch (value) {
            case 0x00:
                return REQUEST;
            case 0x01:
                return RESPONSE;
            case 0x02:
                return HEARTBEAT;
        }
        throw new IllegalArgumentException("Unknown MessageType " + value);
    }
}
