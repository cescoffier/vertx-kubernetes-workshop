package io.vertx.workshop.exercise;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * A verticle using the request-reply event bus delivery mechanism to handle HTTP requests.
 */
public class Exercise5HttpVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
            .requestHandler(req -> {

                // 1 - Retrieve the `name` (query) parameter, set it to `world if null`. You can retrieve the
                // parameter using: `req.getParam()`
                // TODO

                // 2 - Send a message on the event bus using the `send` method. Pass a reply handler receiving the
                // response. As the expected object is a Json structure, you can use `vertx.eventBus()
                // .<JsonObject>send(...`).
                // In the reply handler, you receive an `AsyncResult`. This structure describes the outcome from an
                // asynchronous operation: a success (and a result) or a failure (and a cause). If it's a failure
                // (check with the `failed` method), write a 500 HTTP response with the cause (`cause.getMessage()`) as
                // payload. On success, write the body into the HTTP response.
                // TODO
                
            })
            .listen(8080);
    }
}
