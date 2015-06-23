package io.grapebaba.protocol.v1;

import io.grapebaba.protocol.Protocol;

public class ResponseMessage implements Protocol<Header,Response> {
    @Override
    public Header header() {
        return null;
    }

    @Override
    public Response body() {
        return null;
    }
}
