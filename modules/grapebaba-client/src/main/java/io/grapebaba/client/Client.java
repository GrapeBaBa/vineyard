package io.grapebaba.client;

import io.grapebaba.common.Filter;
import io.grapebaba.common.Service;
import io.grapebaba.common.protocol.v1.RequestMessage;
import io.grapebaba.common.protocol.v1.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.tcp.client.ConnectionProvider;
import rx.Observable;
import rx.functions.Func1;

public abstract class Client implements Func1<RequestMessage, Observable<ResponseMessage>> {

  public static Client newClient(ConnectionProvider<ByteBuf, ByteBuf> connectionProvider,
      Observable<Filter> filters, Observable<Service> service) {
    return ClientImpl.create(connectionProvider, filters, service);
  }

}
