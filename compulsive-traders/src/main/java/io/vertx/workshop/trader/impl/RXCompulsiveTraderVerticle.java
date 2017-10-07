package io.vertx.workshop.trader.impl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.serviceproxy.ProxyHelper;
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

        PortfolioService proxy = PortfolioService.newInstance(ProxyHelper.createProxy(
            io.vertx.workshop.portfolio.PortfolioService.class,
            vertx.getDelegate(),
            "service.portfolio"));

        System.out.println(proxy);

        market = vertx.eventBus().<JsonObject>consumer("market")
            .handler(message -> {                                          // <3>
                JsonObject quote = message.body();
                System.out.println("Trading...");
                TraderUtils.dumbTradingLogic(company, numberOfShares, proxy, quote).subscribe(); // <4>
            });
        // ----
    }


}
