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

package io.grapebaba.vineyard.common;

import rx.Observable;

import java.util.Iterator;

/**
 * A Composition service for chaining filters and service.
 *
 * @param <Req>
 * @param <Res>
 */
public class StackService<Req, Res> implements Service<Req, Res> {
    private final Observable<Filter<Req, Res>> filters;

    private final Service<Req, Res> service;

    /**
     * Constructor.
     *
     * @param filters filters
     * @param service service
     */
    public StackService(Observable<Filter<Req, Res>> filters, Service<Req, Res> service) {
        this.filters = filters;
        this.service = service;
    }

    private static class ChainedService<Req, Res> implements Service<Req, Res> {
        private final Iterator<Filter<Req, Res>> filterIterator;
        private final Filter<Req, Res> filter;
        private final Service<Req, Res> service;

        ChainedService(Iterator<Filter<Req, Res>> filterIterator, Service<Req, Res> service) {
            this.filterIterator = filterIterator;
            this.filter = filterIterator.next();
            this.service = service;
        }

        @Override
        public Observable<Res> call(Req request) {
            if (filterIterator.hasNext()) {
                return filter.call(request, new ChainedService<>(filterIterator, service));
            } else {
                return filter.call(request, service);
            }
        }
    }

    @Override
    public Observable<Res> call(Req req) {
        Iterator<Filter<Req, Res>> iterator = filters.toBlocking().getIterator();

        if (iterator.hasNext()) {
            return new ChainedService<>(iterator, service).call(req);
        } else {
            return service.call(req);
        }
    }

}
