package io.vertx.workshop.exercise.microservice;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.Record;

import static io.vertx.reactivex.CompletableHelper.toObserver;

/**
 * This verticle exposes a HTTP endpoint service. You need to publish it in the service discovery.
 */
public class Exercise1Verticle extends AbstractVerticle{


    private Record record;
    private ServiceDiscovery discovery;

    @Override
    public void start(Future<Void> future) throws Exception {
        // Create a simple HTTP service (using Vert.x Web Router) and publish it in the service discovery.
        // As we want to complete the deployment when the service is exposed (asynchronous operation), we use a
        // `Future` argument to indicate when the deployment is completed. This allows deploying the other verticle
        // after the deployment completion of this one.


        // Create an instance of service discovery
        this.discovery = ServiceDiscovery.create(vertx);

        // Simple HTTP API using Vert.x Web Router.
        Router router = Router.router(vertx);
        router.get("/").handler(rc -> rc.response().end("OK"));
        router.get("/greetings").handler(rc -> rc.response().end("Hello world"));
        router.get("/greetings/:name").handler(rc -> rc.response().end("Hello " + rc.pathParam("name")));

        
        vertx.createHttpServer()
            .requestHandler(router::accept)
            .rxListen(8080)
            // When the server is ready, we publish the service
            .flatMap(this::publish)
            // Store the record, required to un-publish it
            .doOnSuccess(rec -> this.record = rec)
            .toCompletable()
            .subscribe(toObserver(future));
    }

    private Single<Record> publish(HttpServer server) {
        // 1 - Create a service record using `io.vertx.reactivex.servicediscovery.types.HttpEndpoint.createRecord`.
        // This record define the service name ("greetings"), the host ("localhost"), the server port and the root ("/")
        // TODO

        // 2 - Call the rxPublish method with the created record and return the resulting single
        // TODO
        return null; // To be removed
    }

    @Override
    public void stop(Future<Void> future) throws Exception {
        // Unregister the service when the verticle is stopped.
        // As it's an asynchronous operation, we use a `future` parameter to indicate when the operation has been
        // completed.
        discovery.rxUnpublish(record.getRegistration()).subscribe(toObserver(future));
    }
}
