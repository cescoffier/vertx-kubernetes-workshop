package io.vertx.workshop.exercise.microservice;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.HttpEndpoint;

/**
 * A verticle retrieving the service exposed by the Exercise1Verticle.
 * It uses the {@link HttpEndpoint} class to retrieve the {@link io.vertx.reactivex.ext.web.client.WebClient}.
 */
public class Exercise3Verticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

        // 1 - Get the Web Client using the `HttpEndpoint.rxGetWebClient` method. Use the same lambda as in the
        // previous exercise.
        // 2 - Invoke the HTTP service as in the previous exercise
        // 3 - Extract the body as String
        // 4 - Subscribe and display the result on the console
        // TODO
       
    }
}
