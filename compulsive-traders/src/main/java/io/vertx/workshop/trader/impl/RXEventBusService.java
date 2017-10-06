package io.vertx.workshop.trader.impl;

import io.reactivex.Single;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.Record;

import java.util.function.Function;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RXEventBusService {


    public static <T> Single<T> rxGetProxy(ServiceDiscovery discovery, Class<T> clientClass, Function<Record, Boolean>
        filter) {
        return new io.vertx.reactivex.core.impl.AsyncResultSingle<>(handler ->
            EventBusService.getServiceProxy(discovery, filter, clientClass, handler)
        );
    }
}
