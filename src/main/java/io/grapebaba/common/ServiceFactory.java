package io.grapebaba.common;

import io.reactivex.netty.protocol.tcp.client.ConnectionProvider;
import rx.Single;
import rx.functions.Func1;

public interface ServiceFactory<Req, Res>
		extends Func1<ConnectionProvider, Single<Service<Req, Res>>> {

}
