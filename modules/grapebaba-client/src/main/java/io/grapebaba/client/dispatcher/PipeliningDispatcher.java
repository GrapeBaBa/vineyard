package io.grapebaba.client.dispatcher;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import io.grapebaba.client.ClientFactory;
import io.grapebaba.client.config.ClientConfiguration;
import io.grapebaba.common.protocol.PipeliningSupportedProtocol;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import rx.Observable;

public class PipeliningDispatcher implements
    Function<PipeliningSupportedProtocol, CompletableFuture<PipeliningSupportedProtocol>> {
  private static final ConcurrentMap<Integer, CompletableFuture<PipeliningSupportedProtocol>> req2Res =
      new ConcurrentHashMap<>();

  private final TcpClient<PipeliningSupportedProtocol, PipeliningSupportedProtocol> client =
      ClientFactory.newClient(new ClientConfiguration());

  @Override
  public CompletableFuture<PipeliningSupportedProtocol> apply(PipeliningSupportedProtocol protocol) {
    CompletableFuture<PipeliningSupportedProtocol> responseCompletableFuture =
        new CompletableFuture<>();
    req2Res.putIfAbsent(protocol.getOpaque(), responseCompletableFuture);

    client
        .createConnectionRequest()
        .flatMap(
            protocolProtocolConnection -> protocolProtocolConnection
                .write(Observable.just(protocol)).cast(PipeliningSupportedProtocol.class)
                .mergeWith(protocolProtocolConnection.getInput())).subscribe(response -> {
        req2Res.get(response.getOpaque()).complete(response);
    });

    return responseCompletableFuture;
  }
}
