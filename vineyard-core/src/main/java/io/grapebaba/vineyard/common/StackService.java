package io.grapebaba.vineyard.common;

import rx.Observable;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <Req>
 * @param <Res>
 */
public class StackService<Req, Res> implements Service<Req, Res> {
    private final List<Filter<Req, Res>> filters;

    private final Service<Req, Res> service;

    public StackService(List<Filter<Req, Res>> filters, Service<Req, Res> service) {
        this.filters = filters;
        this.service = service;
    }

    private static class ChainedService<Req, Res> implements Service<Req, Res> {
        private final Iterator<Filter<Req, Res>> filterIterator;
        private final Filter<Req, Res> filter;
        private final Service<Req, Res> service;

        public ChainedService(Iterator<Filter<Req, Res>> filterIterator, Service<Req, Res> service) {
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
        if (filters.isEmpty()) {
            return service.call(req);
        } else {
            return new ChainedService<>(filters.iterator(), service).call(req);
        }
    }

}
