package io.grapebaba.common;

import rx.Observable;
import rx.functions.Func2;

public interface Filter<Req, Res> extends
    Func2<Req, Service<Req, Observable<Res>>, Observable<Res>> {
}
