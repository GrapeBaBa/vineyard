package io.grapebaba.protocol.v1;

public class ResponseMessage extends ProtocolMessage<DefaultProtocolV1Header, Response> {
  @Override
  public DefaultProtocolV1Header header() {
    return null;
  }

  @Override
  public Response body() {
    return null;
  }
}
