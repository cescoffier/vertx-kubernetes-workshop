package io.vertx.workshop.dashboard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import in.yunyul.vertx.console.base.WebConsoleRegistry;
import in.yunyul.vertx.console.circuitbreakers.CircuitBreakersConsolePage;
import in.yunyul.vertx.console.metrics.MetricsConsolePage;
import in.yunyul.vertx.console.services.ServicesConsolePage;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.MessageSource;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DashboardVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        MetricRegistry dropwizardRegistry = SharedMetricRegistries.getOrCreate(
            System.getProperty("vertx.metrics.options.registryName")
        );

        ServiceDiscovery.create(vertx, discovery -> {
            WebConsoleRegistry.create("/admin")
                // Add pages
                .addPage(MetricsConsolePage.create(dropwizardRegistry))
                .addPage(ServicesConsolePage.create(discovery))
                .addPage(CircuitBreakersConsolePage.create())
                .setCacheBusterEnabled(true) // Adds random query string to scripts
                // Mount to router
                .mount(vertx, router);

            vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);

            vertx.eventBus().consumer("market", msg -> {
                    System.out.println(msg.body());
            });
        });
    }
}
