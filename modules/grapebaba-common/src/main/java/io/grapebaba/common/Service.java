package io.grapebaba.common;

import rx.Observable;
import rx.functions.Func1;

public interface Service<Req, Res> extends Func1<Req, Observable<Res>> {
}
