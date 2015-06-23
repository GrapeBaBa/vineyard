package io.grapebaba.protocol;

public enum SerializerType {
    FST,
    KRYO;

    public byte getValue() {
        switch (this) {
            case FST:
                return 0x00;
            case KRYO:
                return 0x01;
        }

        throw new IllegalArgumentException("Unknown SerializerType" + this);
    }


    public static SerializerType valueOf(byte value) {
        switch (value) {
            case 0x00:
                return FST;
            case 0x01:
                return KRYO;
        }
        throw new IllegalArgumentException("Unknown SerializerType" + value);
    }
}
