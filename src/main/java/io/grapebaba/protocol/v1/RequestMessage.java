package io.grapebaba.protocol.v1;

public class RequestMessage extends ProtocolMessage<Header,Request> {
    @Override
    public Header header() {
        return null;
    }

    @Override
    public Request body() {
        return null;
    }
}
