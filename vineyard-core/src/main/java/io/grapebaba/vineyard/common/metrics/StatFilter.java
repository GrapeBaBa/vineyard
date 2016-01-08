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
