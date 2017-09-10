package io.vertx.workshop.exercise.microservice;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;

/**
 * A verticle retrieving the service exposed by the Exercise1Verticle.
 * It uses the low-level API.
 */
public class Exercise2Verticle extends AbstractVerticle {

    private ServiceDiscovery discovery;

    @Override
    public void start() throws Exception {
        discovery = ServiceDiscovery.create(vertx);

        // 1 - Get the service record using `rxGetRecord`. Pass the lambda `svc -> svc.getName().equals("greetings")` as
        // parameter to retrieve the service with the name "greetings"
        // 2 - With the record (`.map`), get the service reference using `discovery.getReference`
        // 3 - With the reference (`.map`), get a WebClient (Vert.x http client) using `ref.getAs(WebClient.class)`
        // 4 - With the client (`.flatMap`), invoke the service using: `client.get("/greetings/vert.x-low-level-api").rxSend()`
        // 5 - With the response (`.map`), extract the body as string (`bodyAsString` method)
        // 6 - Finally subscribe and print the result on the console
        // TODO
       
    }
}
