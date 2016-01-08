/*
 * Copyright 2015 281165273grape@gmail.com
 *
 * Licensed under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.grapebaba.vineyard.grape.metrics;

import com.codahale.metrics.Counter;
import io.grapebaba.vineyard.common.Filter;
import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.metrics.Metrics;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import rx.Observable;

/**
 * Grape rpc metrics filter.
 */
public class GrapeStatFilter implements Filter<RequestMessage, ResponseMessage> {
    @Override
    public Observable<ResponseMessage> call(
            RequestMessage requestMessage,
            Service<RequestMessage, ResponseMessage> requestMessageResponseMessageService) {
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
