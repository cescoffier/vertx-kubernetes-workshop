package io.vertx.workshop.portfolio.impl;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.Record;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.workshop.portfolio.PortfolioService;

import static io.vertx.workshop.portfolio.PortfolioService.ADDRESS;

/**
 * A verticle publishing the portfolio service.
 */
public class PortfolioVerticle extends AbstractVerticle {

  private Record record;
  private ServiceDiscovery discovery;

  @Override
  public void start() {

    ServiceDiscovery.create(vertx, discovery -> {
      this.discovery = discovery;
      // Create the service object
      PortfolioServiceImpl service = new PortfolioServiceImpl(vertx, discovery, config().getDouble("money", 10000.00));

      // Register the service proxy on the event bus
      ProxyHelper.registerService(PortfolioService.class, vertx.getDelegate(), service, ADDRESS);

      Record record = EventBusService.createRecord("portfolio", ADDRESS, PortfolioService.class.getName());
      discovery.publish(record, ar -> {
        if (ar.succeeded()) {
          this.record = record;
          System.out.println("Portfolio service published");

          // Used for health check
          vertx.createHttpServer().requestHandler(req -> req.response().end("OK")).listen(8080);
        } else {
          ar.cause().printStackTrace();
        }
      });

    });
  }

  @Override
  public void stop() throws Exception {
    if (record != null) {
      discovery.unpublish(record.getRegistration(), v -> {});
    }
    discovery.close();
  }
}
