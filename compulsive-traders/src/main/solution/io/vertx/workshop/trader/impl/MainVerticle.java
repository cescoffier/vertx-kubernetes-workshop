package io.vertx.workshop.trader.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

/**
 * The main verticle creating compulsive traders.
 */
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    
    vertx.deployVerticle(CallbackTraderVerticle.class.getName());
//    vertx.deployVerticle(RXCompulsiveTraderVerticle.class.getName());

    // TODO Kotlin
//    vertx.deployVerticle("GroovyCompulsiveTraderVerticle.groovy");

  }
}
