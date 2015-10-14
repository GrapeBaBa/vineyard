package io.grapebaba.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.grapebaba.common.Filter;
import io.grapebaba.common.Service;
import io.grapebaba.common.protocol.v1.RequestMessage;
import io.grapebaba.common.protocol.v1.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.tcp.client.ConnectionProvider;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import rx.Observable;
import rx.subjects.ReplaySubject;

public final class ClientImpl extends Client {

  private final TcpClient<RequestMessage, ResponseMessage> client;

  private final Observable<Filter> filters;

  private final Observable<Service> service;

  private ClientImpl(TcpClient<RequestMessage, ResponseMessage> client, Observable<Filter> filters,
      Observable<Service> service) {
    this.client = client;
    this.filters = filters;
    this.service = service;
  }

  public static Client create(final ConnectionProvider<ByteBuf, ByteBuf> connectionProvider,
      final Observable<Filter> filters, final Observable<Service> service) {
    final Observable<Filter> defaultFilters = Observable.empty();
    final Observable<Service> defaultService = Observable.just(new DefaultService())
    return new ClientImpl(TcpClient.newClient(connectionProvider).pipelineConfigurator(entries -> {

    }), filters.switchIfEmpty(defaultFilters), service.switchIfEmpty(defaultService));
  }

  @Override
  public Observable<ResponseMessage> call(RequestMessage requestMessage) {
    return null;
  }

  private static class DefaultService implements Service<RequestMessage, ResponseMessage> {
    private static final ConcurrentMap<Integer, ReplaySubject<ResponseMessage>> req2Res =
        new ConcurrentHashMap<>();

    private final TcpClient<RequestMessage, ResponseMessage> client;

    public DefaultService(TcpClient<RequestMessage, ResponseMessage> client) {
      this.client = client;
    }

    @Override
    public Observable<ResponseMessage> call(RequestMessage requestMessage) {
      ReplaySubject<ResponseMessage> response = ReplaySubject.create();
      req2Res.putIfAbsent(requestMessage.getOpaque(), response);

      client
          .createConnectionRequest()
          .flatMap(
              connection -> connection.write(Observable.just(requestMessage))
                  .cast(ResponseMessage.class).mergeWith(connection.getInput()))
          .subscribe(
              responseMessage -> {
                ReplaySubject<ResponseMessage> responseMessageReplaySubject =
                    req2Res.get(responseMessage.getOpaque());
                responseMessageReplaySubject.onNext(responseMessage);
                responseMessageReplaySubject.onCompleted();
              });

      return response;
    }
  }
}
