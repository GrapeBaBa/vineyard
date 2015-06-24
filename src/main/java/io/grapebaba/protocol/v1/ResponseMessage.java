package io.grapebaba.protocol.v1;

public class ResponseMessage extends ProtocolMessage<Header, Response> {
    @Override
    public Header header() {
        return null;
    }

    @Override
    public Response body() {
        return null;
    }
}
