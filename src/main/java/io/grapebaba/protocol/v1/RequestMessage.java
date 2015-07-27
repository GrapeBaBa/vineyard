package io.grapebaba.protocol.v1;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * The default rpc protocol request message.
 */
public class RequestMessage extends ProtocolMessage<DefaultProtocolV1Header, Request> {

  private DefaultProtocolV1Header defaultProtocolV1Header;

  private Request request;

  @Override
  public DefaultProtocolV1Header header() {
    return this.defaultProtocolV1Header;
  }

  @Override
  public Request body() {
    return this.request;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    RequestMessage that = (RequestMessage) obj;
    return Objects.equals(defaultProtocolV1Header, that.defaultProtocolV1Header)
        && Objects.equals(request, that.request);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultProtocolV1Header, request);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("defaultProtocolV1Header", defaultProtocolV1Header)
        .add("request", request).toString();
  }

  public static class RequestMessageBuilder {
    private RequestMessage requestMessage;

    private RequestMessageBuilder() {
      requestMessage = new RequestMessage();
    }

    public RequestMessageBuilder withDefaultProtocolV1Header(
        DefaultProtocolV1Header defaultProtocolV1Header) {
      requestMessage.defaultProtocolV1Header = defaultProtocolV1Header;
      return this;
    }

    public RequestMessageBuilder withRequest(Request request) {
      requestMessage.request = request;
      return this;
    }

    public static RequestMessageBuilder requestMessage() {
      return new RequestMessageBuilder();
    }

    public RequestMessage build() {
      return requestMessage;
    }
  }
}
