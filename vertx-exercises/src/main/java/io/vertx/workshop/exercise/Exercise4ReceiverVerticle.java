package io.vertx.workshop.exercise;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * A verticle receiving messages.
 */
public class Exercise4ReceiverVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // Retrieve the event bus and register a consumer on the "greetings" address. For each message, print it on
        // the console. You can retrieve the message body using `body()`. Use the method `encodePrettily`
        // on the retrieved Json body to print it nicely.
        // TODO
    }
}
