package io.grapebaba.server;

import io.grapebaba.protocol.Protocol;

public interface ServerHandler<P extends Protocol> {

  P handle(P protocol);
}
