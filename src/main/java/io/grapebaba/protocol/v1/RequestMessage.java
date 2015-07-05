package io.grapebaba.protocol.v1;

public class RequestMessage extends ProtocolMessage<DefaultProtocolV1Header, Request> {
  @Override
  public DefaultProtocolV1Header header() {
    return null;
  }

  @Override
  public Request body() {
    return null;
  }
}
