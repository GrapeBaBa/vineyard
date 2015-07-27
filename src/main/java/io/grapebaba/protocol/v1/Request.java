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

import java.util.Arrays;
import java.util.Objects;

/**
 * Default rpc protocol request structure.
 */
public class Request implements Body {
  private Integer opaque;

  private Integer timeout;

  private String beanName;

  private String methodName;

  private Object[] arguments;

  public Object[] getArguments() {
    return Arrays.copyOf(arguments, arguments.length);
  }

  public String getBeanName() {
    return beanName;
  }

  public String getMethodName() {
    return methodName;
  }

  public Integer getOpaque() {
    return opaque;
  }

  public Integer getTimeout() {
    return timeout;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Request request = (Request) obj;
    return Objects.equals(opaque, request.opaque) && Objects.equals(timeout, request.timeout)
        && Objects.equals(beanName, request.beanName)
        && Objects.equals(methodName, request.methodName)
        && Arrays.deepEquals(arguments, request.arguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opaque, timeout, beanName, methodName, arguments);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("arguments", arguments).add("opaque", opaque)
        .add("timeout", timeout).add("beanName", beanName).add("methodName", methodName).toString();
  }

  public static class RequestBuilder {
    private Request request;

    private RequestBuilder() {
      request = new Request();
    }

    public RequestBuilder withOpaque(Integer opaque) {
      request.opaque = opaque;
      return this;
    }

    public RequestBuilder withTimeout(Integer timeout) {
      request.timeout = timeout;
      return this;
    }

    public RequestBuilder withBeanName(String beanName) {
      request.beanName = beanName;
      return this;
    }

    public RequestBuilder withMethodName(String methodName) {
      request.methodName = methodName;
      return this;
    }

    public RequestBuilder withArguments(Object[] arguments) {
      request.arguments = arguments;
      return this;
    }

    public static RequestBuilder request() {
      return new RequestBuilder();
    }

    public Request build() {
      return request;
    }
  }
}
