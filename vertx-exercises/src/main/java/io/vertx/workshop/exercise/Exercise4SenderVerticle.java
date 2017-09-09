package io.vertx.workshop.exercise;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * A verticle periodically sending a message on the event bus.
 */
public class Exercise4SenderVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // Retrieve the event bus
        EventBus eventBus = vertx.eventBus();

        // Execute the given handler every 2000 ms
        vertx.setPeriodic(2000, l -> {
            // Use the eventBus() method to retrieve the event bus and send a "{"message":hello"} JSON message on the
            // "greetings" address.

            // 1 - Create the JSON object using the JsonObject class, and `put` the 'message':'hello' entry
            // TODO

            // 2 - Use the `send` method of the event bus to _send_ the message. Messages sent with the `send` method
            // are received by a single consumer. Messages sent with the `publish` method are received by all
            // registered consumers.
            // TODO
            
        });
    }
}
