package io.vertx.workshop.quote;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * This verticle exposes a HTTP endpoint to retrieve the current / last values of the maker data (quotes).
 */
public class RestQuoteAPIVerticle extends AbstractVerticle {

    private Map<String, JsonObject> quotes = new HashMap<>();

    @Override
    public void start(Future<Void> future) throws Exception {
        // Get the stream of messages sent on the "market" address
        vertx.eventBus().<JsonObject>consumer(GeneratorConfigVerticle.ADDRESS).toFlowable()
            // TODO Extract the body of the message using `.map(msg -> {})`
            // ----
            //
            // ----
            // TODO For each message, populate the `quotes` map with the received quote. Use `.doOnNext(json -> {})`
            // Quotes are json objects you can retrieve from the message body
            // The map is structured as follows: name -> quote
            // ----
            //
            // ----
            .subscribe();

        HttpServer server = vertx.createHttpServer();
        server.requestStream().toFlowable()
            .doOnNext(request -> {
                HttpServerResponse response = request.response()
                    .putHeader("content-type", "application/json");

                // TODO Handle the HTTP request
                // The request handler returns a specific quote if the `name` parameter is set, or the whole map if none.
                // To write the response use: `response.end(content)`
                // If the name is set but not found, you should return 404 (use response.setStatusCode(404)).
                // To encode a Json object, use the `encorePrettily` method
                // ----

                // Remove this line
                response.end(Json.encodePrettily(quotes));
                
                // ----
            })
        .subscribe();

        server.rxListen(config().getInteger("http.port", 8080))
            .toCompletable()
            .subscribe(CompletableHelper.toObserver(future));
    }
}
