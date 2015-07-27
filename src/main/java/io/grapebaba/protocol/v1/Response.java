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

import io.grapebaba.protocol.Body;

import java.util.Objects;

/**
 * Default rpc protocol response structure.
 */
public class Response implements Body {

  private Integer opaque;

  private Object result;

  public Integer getOpaque() {
    return opaque;
  }

  public Object getResult() {
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Response response = (Response) obj;
    return Objects.equals(opaque, response.opaque) && Objects.equals(result, response.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opaque, result);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("opaque", opaque).add("result", result).toString();
  }

  public static class ResponseBuilder {
    private Response response;

    private ResponseBuilder() {
      response = new Response();
    }

    public ResponseBuilder withOpaque(Integer opaque) {
      response.opaque = opaque;
      return this;
    }

    public ResponseBuilder withResult(Object result) {
      response.result = result;
      return this;
    }

    public static ResponseBuilder response() {
      return new ResponseBuilder();
    }

    public Response build() {
      return response;
    }
  }
}
