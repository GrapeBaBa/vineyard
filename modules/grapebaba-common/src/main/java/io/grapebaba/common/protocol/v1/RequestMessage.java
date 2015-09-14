/*
 * Copyright 2015 281165273grape@gmail.com
 *
 * Licensed under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.grapebaba.common.protocol.v1;

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
