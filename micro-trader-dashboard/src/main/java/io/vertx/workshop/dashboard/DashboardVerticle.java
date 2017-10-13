package io.vertx.workshop.dashboard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import in.yunyul.vertx.console.base.WebConsoleRegistry;
import in.yunyul.vertx.console.circuitbreakers.CircuitBreakersConsolePage;
import in.yunyul.vertx.console.metrics.MetricsConsolePage;
import in.yunyul.vertx.console.services.ServicesConsolePage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class DashboardVerticle extends AbstractVerticle {

    private ServiceDiscovery discovery;

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions();
        options
            .addOutboundPermitted(new PermittedOptions().setAddress("market"))
            .addOutboundPermitted(new PermittedOptions().setAddress("portfolio"))
            .addOutboundPermitted(new PermittedOptions().setAddress("service.portfolio"))
            .addInboundPermitted(new PermittedOptions().setAddress("service.portfolio"))
            .addOutboundPermitted(new PermittedOptions().setAddress("vertx.circuit-breaker"));

        sockJSHandler.bridge(options);
        router.route("/eventbus/*").handler(sockJSHandler);

        // Last operations
        router.get("/operations").handler(this::callAuditService);
        router.get("/health").handler(rc -> rc.response().end("OK"));


        MetricRegistry dropwizardRegistry = SharedMetricRegistries.getOrCreate(
            System.getProperty("vertx.metrics.options.registryName")
        );

        ServiceDiscovery.create(vertx, discovery -> {
            this.discovery = discovery;
            WebConsoleRegistry.create("/admin")
                // Add pages
                .addPage(MetricsConsolePage.create(dropwizardRegistry))
                .addPage(new TraderPage())
                .addPage(ServicesConsolePage.create(discovery))
                .addPage(CircuitBreakersConsolePage.create())
                .setCacheBusterEnabled(true) // Adds random query string to scripts
                // Mount to router
                .mount(vertx, router);

            retrieveAuditService();
            vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
        });

    }


    private Future<WebClient> retrieveAuditService() {
        Future<WebClient> future = Future.future();
        HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "audit-service"), future);
        return future;
    }


    private void callAuditService(RoutingContext context) {
        retrieveAuditService()
            .setHandler(ar -> {
                if (ar.failed() || ar.result() == null) {
                    context.response()
                        .putHeader("content-type", "application/json")
                        .setStatusCode(200)
                        .end(new JsonObject().put("message", "No audit service").encode());
                } else {
                    ar.result().get("/")
                        .as(BodyCodec.jsonArray())
                        .timeout(5000)
                        .send(res -> {
                            if (res.succeeded()) {
                                HttpResponse<JsonArray> response = res.result();
                                JsonArray operations = new JsonArray();
                                for (Object entry : response.body()) {
                                    JsonObject json = (JsonObject) entry;
                                    operations.add(
                                        new JsonObject()
                                            .put("type", json.getString("action"))
                                            .put("company", json.getJsonObject("quote").getString("name"))
                                            .put("amount", json.getInteger("amount"))
                                    );
                                }
                                context.response()
                                    .putHeader("content-type", "application/json")
                                    .setStatusCode(200)
                                    .end(operations.toBuffer());
                            } else {
                                ar.cause().printStackTrace();
                                context.response()
                                    .putHeader("content-type", "application/json")
                                    .setStatusCode(200)
                                    .end(new JsonObject()
                                        .put("message", "No audit service (" + ar.cause().getMessage() + ")").encode());
                            }
                        });
                }
            });
    }
}
