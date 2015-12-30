package io.grapebaba.vineyard.common.metrics;

import com.codahale.metrics.Counter;
import io.grapebaba.vineyard.common.Filter;
import io.grapebaba.vineyard.common.Service;
import rx.Observable;

/**
 * Basic metrics filter.
 *
 * @param <Req>
 * @param <Res>
 */
public class StatFilter<Req, Res> implements Filter<Req, Res> {
    private static final Counter FAILURE_COUNTER = Metrics.REGISTRY.counter("failures");

    private static final Counter SUCCESS_COUNTER = Metrics.REGISTRY.counter("successes");

    private static final Counter REQUEST_COUNTER = Metrics.REGISTRY.counter("requests");

    @Override
    public Observable<Res> call(Req req, Service<Req, Res> reqResService) {
        return reqResService.call(req)
                .doOnNext(res -> SUCCESS_COUNTER.inc())
                .doOnError(throwable -> FAILURE_COUNTER.inc())
                .doOnSubscribe(REQUEST_COUNTER::inc);
    }
}
