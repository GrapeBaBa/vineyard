package io.grapebaba.server;

import io.grapebaba.protocol.Protocol;

/**
 * The server handler interface.
 * 
 * @param <P> protocol
 */
public interface ServerHandler<P extends Protocol> {

  P handle(P protocol);

  void registerServices(String packageName);
}
