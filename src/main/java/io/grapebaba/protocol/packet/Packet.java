package io.grapebaba.protocol.packet;

import io.grapebaba.protocol.Protocol;

public class Packet implements Protocol<Header,Body> {
    @Override
    public Header header() {
        return null;
    }

    @Override
    public Body body() {
        return null;
    }

    @Override
    public byte getMagicNumber() {
        return 0;
    }
}
