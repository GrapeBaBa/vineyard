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
