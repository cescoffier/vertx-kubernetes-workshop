package io.vertx.workshop.exercise;

import io.vertx.core.Vertx;

/**
 * Launcher for the Exercise 6.
 */
public class Exercise6 {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Exercise6HttpVerticle.class.getName());
        // Keep using the once developed previously
        vertx.deployVerticle(Exercise5ProcessorVerticle.class.getName());
    }

}
