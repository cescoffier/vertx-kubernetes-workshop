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
 * A compulsive trader developed with RX Java 2.
 */
public class RXCompulsiveTraderVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> future) {
        String company = TraderUtils.pickACompany();
        int numberOfShares = TraderUtils.pickANumber();
        System.out.println("Java-RX compulsive trader configured for company " + company + " and shares: " +
            numberOfShares);


        ServiceDiscovery.create(vertx, discovery -> {
            Single<PortfolioService> retrieveThePortfolioService = RXEventBusService.rxGetProxy(discovery, PortfolioService.class,
                rec -> rec.getName().equalsIgnoreCase("portfolio"));

            Single<MessageConsumer<JsonObject>> retrieveTheMarket = MessageSource.rxGetConsumer(discovery,
                rec -> rec.getName().equalsIgnoreCase("market-data"));

            //TODO
            //----

            // TODO 1 - "wait" for both single to be completed (using Single.zip or Single.zipWith methods)
            retrieveThePortfolioService.zipWith(retrieveTheMarket, (ps, consumer) -> {

            // TODO 2 - When both single have completed, attach the handler to the message consumer to execute the
                // trading logic

                consumer.handler(message ->
                    // TODO 3 - Use the TraderUtils.drumpTradingLogic method returning a Completable. Don't forget to
                    // subscribe to it, or nothing will happen. Return "true" to comply with the "zip" operator
                    // signature.
                    
                    TraderUtils.dumbTradingLogic(company, numberOfShares, ps, message.body()).subscribe());
                // We need to return something as requested by the "zip" signature.
                return true;
            })

                // TODO 4 - Transform the output into a Completable (toCompletable) and subscribe to it using:
                //.subscribe(CompletableHelper.toObserver(future)) - it reports the failure or success to the `done`
                // future.
                .toCompletable()
                .subscribe(CompletableHelper.toObserver(future));
            //----

        });
    }


}
