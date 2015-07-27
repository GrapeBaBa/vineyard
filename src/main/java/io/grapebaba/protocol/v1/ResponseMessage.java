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

package io.grapebaba.protocol.v1;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * The default rpc protocol response message.
 */
public class ResponseMessage extends ProtocolMessage<DefaultProtocolV1Header, Response> {
  private DefaultProtocolV1Header defaultProtocolV1Header;

  private Response response;

  @Override
  public DefaultProtocolV1Header header() {
    return defaultProtocolV1Header;
  }

  @Override
  public Response body() {
    return response;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ResponseMessage that = (ResponseMessage) obj;
    return Objects.equals(defaultProtocolV1Header, that.defaultProtocolV1Header)
        && Objects.equals(response, that.response);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultProtocolV1Header, response);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("defaultProtocolV1Header", defaultProtocolV1Header)
        .add("response", response).toString();
  }

  public static class ResponseMessageBuilder {
    private ResponseMessage responseMessage;

    private ResponseMessageBuilder() {
      responseMessage = new ResponseMessage();
    }

    public ResponseMessageBuilder withDefaultProtocolV1Header(
        DefaultProtocolV1Header defaultProtocolV1Header) {
      responseMessage.defaultProtocolV1Header = defaultProtocolV1Header;
      return this;
    }

    public ResponseMessageBuilder withResponse(Response response) {
      responseMessage.response = response;
      return this;
    }

    public static ResponseMessageBuilder responseMessage() {
      return new ResponseMessageBuilder();
    }

    public ResponseMessage build() {
      return responseMessage;
    }
  }
}
