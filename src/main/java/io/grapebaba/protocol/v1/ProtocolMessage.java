package io.grapebaba.protocol.v1;

import io.grapebaba.protocol.Body;
import io.grapebaba.protocol.Protocol;

public abstract class ProtocolMessage<H extends Header, B extends Body> implements Protocol<H, B> {
    public static final byte MAGIC_NUMBER = (byte) 0x76;

    @Override
    public byte getMagicNumber() {
        return MAGIC_NUMBER;
    }
}
