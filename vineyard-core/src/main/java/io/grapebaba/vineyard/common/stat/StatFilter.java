package io.grapebaba.vineyard.common.stat;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import io.grapebaba.vineyard.common.Filter;
import io.grapebaba.vineyard.common.Service;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Basic metrics filter.
 *
 * @param <Req>
 * @param <Res>
 */
public class StatFilter<Req, Res> implements Filter<Req, Res> {
    private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

    private static final Counter FAILURE_COUNTER = METRIC_REGISTRY.counter("failures");

    private static final Counter SUCCESS_COUNTER = METRIC_REGISTRY.counter("successes");

    private static final Counter REQUEST_COUNTER = METRIC_REGISTRY.counter("requests");

    static {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(METRIC_REGISTRY).build();
        reporter.start(300, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Res> call(Req req, Service<Req, Res> reqResService) {
        return reqResService.call(req)
                .doOnNext(res -> SUCCESS_COUNTER.inc())
                .doOnError(throwable -> FAILURE_COUNTER.inc())
                .doOnSubscribe(REQUEST_COUNTER::inc);
    }
}
