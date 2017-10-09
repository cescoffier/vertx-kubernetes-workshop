package io.vertx.workshop.trader.impl

import io.vertx.servicediscovery.Record
import io.vertx.servicediscovery.ServiceDiscovery
import io.vertx.servicediscovery.types.EventBusService
import io.vertx.workshop.portfolio.PortfolioService


class KotlinCompulsiveTraderVerticle : io.vertx.core.AbstractVerticle() {

    private var discovery: ServiceDiscovery? = null

    override fun start() {
        ServiceDiscovery.create(vertx, { discovery ->
            this.discovery = discovery

            EventBusService.getServiceProxy(
                    discovery,
                    fun(rec: Record) : Boolean { return rec.getName().equals("portfolio")},
                    PortfolioService::class.java, {
                                ar -> println("done")
                            }
            )
            
        });
    }
}