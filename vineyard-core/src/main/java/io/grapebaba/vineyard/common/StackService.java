package io.grapebaba.vineyard.common;

import rx.Observable;

import java.util.Iterator;

/**
 * @param <Req>
 * @param <Res>
 */
public class StackService<Req, Res> implements Service<Req, Res> {
    private final Observable<Filter<Req, Res>> filters;

    private final Service<Req, Res> service;

    public StackService(Observable<Filter<Req, Res>> filters, Service<Req, Res> service) {
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
        Iterator<Filter<Req, Res>> iterator = filters.toBlocking().getIterator();

        if (iterator.hasNext()) {
            return new ChainedService<>(iterator, service).call(req);
        } else {
            return service.call(req);
        }
    }

}
