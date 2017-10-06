package io.vertx.workshop.trader.impl;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.MessageSource;
import io.vertx.workshop.portfolio.reactivex.PortfolioService;

/**
 * A compulsive trader...
 */
public class RXCompulsiveTraderVerticle extends AbstractVerticle {

  private MessageConsumer<JsonObject> market;
  private PortfolioService portfolio;

  @Override
  public void start(Future<Void> future) {
    //TODO
    //----

    String company = TraderUtils.pickACompany();
    int numberOfShares = TraderUtils.pickANumber();
    System.out.println("Java compulsive trader configured for company " + company + " and shares: " + numberOfShares);

    ServiceDiscovery.create(vertx, discovery -> {
      Single<MessageConsumer<JsonObject>> market = MessageSource.rxGetConsumer(discovery,
          svc -> svc.getName().equals("market-data"));
      Single<PortfolioService> portfolio = RXEventBusService.rxGetProxy(discovery, PortfolioService.class, svc -> svc.getName().equalsIgnoreCase
          ("portfolio"));

      Single.zip(market, portfolio, (m, p) -> {
        this.market = m;
        this.portfolio = p;

        // Listen the market...
        this.market.handler(message -> {                                          // <3>
          JsonObject quote = message.body();
          TraderUtils.dumbTradingLogic(company, numberOfShares, this.portfolio, quote).subscribe(); // <4>
        });

        return true;
      })
          .toCompletable()
          .subscribe(CompletableHelper.toObserver(future));
    });
    // ----
  }


}
