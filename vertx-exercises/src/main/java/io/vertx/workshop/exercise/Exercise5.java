package io.vertx.workshop.exercise;

import io.vertx.core.Vertx;

/**
 * Launcher for the Exercise 5.
 */
public class Exercise5 {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Exercise5HttpVerticle.class.getName());
        vertx.deployVerticle(Exercise5ProcessorVerticle.class.getName());
    }

}
