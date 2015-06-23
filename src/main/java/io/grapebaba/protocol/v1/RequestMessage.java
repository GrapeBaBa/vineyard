package io.grapebaba.protocol.v1;

import io.grapebaba.protocol.Protocol;

public class RequestMessage implements Protocol<Header, Request> {
    @Override
    public Header header() {
        return null;
    }

    @Override
    public Request body() {
        return null;
    }
}
