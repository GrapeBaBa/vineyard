package io.grapebaba.client.dispatcher;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import io.grapebaba.client.config.ClientConfiguration;
import io.grapebaba.common.protocol.PipliningSupportedProtocol;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import rx.Observable;

public class PipeliningDispatcher implements
    Fu<PipliningSupportedProtocol, CompletableFuture<PipliningSupportedProtocol>> {
  private static final ConcurrentMap<Integer, CompletableFuture<PipliningSupportedProtocol>> req2Res =
      new ConcurrentHashMap<>();

  private final TcpClient<PipliningSupportedProtocol, PipliningSupportedProtocol> client =
      ClientFactory.newClient(new ClientConfiguration());

  @Override
  public CompletableFuture<PipliningSupportedProtocol> apply(PipliningSupportedProtocol protocol) {
    CompletableFuture<PipliningSupportedProtocol> responseCompletableFuture =
        new CompletableFuture<>();
    req2Res.putIfAbsent(protocol.getOpaque(), responseCompletableFuture);

    client
        .createConnectionRequest()
        .flatMap(
            protocolProtocolConnection -> protocolProtocolConnection
                .write(Observable.just(protocol)).cast(PipliningSupportedProtocol.class)
                .mergeWith(protocolProtocolConnection.getInput())).subscribe(response -> {
        req2Res.get(response.getOpaque()).complete(response);
    });

    return responseCompletableFuture;
  }
}
