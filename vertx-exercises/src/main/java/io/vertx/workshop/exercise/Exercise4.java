package io.vertx.workshop.exercise;

import io.vertx.core.Vertx;

/**
 * Launcher for the Exercise 4, it just deploys the 2 verticles.
 */
public class Exercise4 {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Exercise4SenderVerticle.class.getName());
        vertx.deployVerticle(Exercise4ReceiverVerticle.class.getName());
    }

}
