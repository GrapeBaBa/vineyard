package io.grapebaba.client.dispatcher;

import java.util.concurrent.CompletableFuture;

import io.grapebaba.common.protocol.Protocol;

@FunctionalInterface
public interface Dispatcher {

  <Req extends Protocol, Res extends Protocol> CompletableFuture<Res> dispatch(Req protocol);
}
