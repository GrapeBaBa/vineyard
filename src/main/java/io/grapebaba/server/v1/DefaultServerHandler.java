package io.grapebaba.server.v1;

import com.google.common.collect.Maps;

import io.grapebaba.protocol.Protocol;

import java.util.Map;

public class DefaultServerHandler implements io.grapebaba.server.ServerHandler<Protocol> {

  private static Map<String, Object> registry = Maps.newHashMap();

  @Override
  public Protocol handle(Protocol protocol) {
    return null;
  }
}
