package io.grapebaba.vineyard.grape.metrics;

import com.codahale.metrics.Counter;
import io.grapebaba.vineyard.common.Filter;
import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.metrics.Metrics;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import rx.Observable;

/**
 *
 */
public class GrapeStatFIlter implements Filter<RequestMessage, ResponseMessage> {
    @Override
    public Observable<ResponseMessage> call(RequestMessage requestMessage, Service<RequestMessage, ResponseMessage> requestMessageResponseMessageService) {
        final String beanName = requestMessage.getBeanName();
        final String methodName = requestMessage.getMethodName();
        final String serviceName = beanName + "/" + methodName;

        return requestMessageResponseMessageService.call(requestMessage).doOnNext(responseMessage -> {
            Counter counter = Metrics.REGISTRY.counter(serviceName + "/successes");
            counter.inc();
        }).doOnError(throwable -> {
            Counter counter = Metrics.REGISTRY.counter(serviceName + "/failures");
            counter.inc();
        }).doOnSubscribe(() -> {
            Counter counter = Metrics.REGISTRY.counter(serviceName + "/requests");
            counter.inc();
        });
    }
}
