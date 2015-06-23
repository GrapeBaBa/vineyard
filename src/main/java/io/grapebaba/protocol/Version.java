package io.grapebaba.protocol;

public enum Version {
    V1;

    public byte getValue() {
        switch (this) {
            case V1:
                return 0x00;
        }

        throw new IllegalArgumentException("Unknown Version" + this);
    }


    public static Version valueOf(byte value) {
        switch (value) {
            case 0x00:
                return V1;
        }
        throw new IllegalArgumentException("Unknown Version " + value);
    }
}
