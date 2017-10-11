package io.vertx.workshop.currency;

import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.circuitbreaker.CircuitBreaker;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.EventBusService;
import io.vertx.reactivex.servicediscovery.types.HttpEndpoint;
import io.vertx.workshop.portfolio.reactivex.PortfolioService;

import static io.vertx.workshop.currency.Helpers.toObserver;

/**
 * Verticle acting as a proxy between our application and the flaky 3rd party currency service.
 */
public class CurrencyServiceProxy extends AbstractVerticle {

    private CircuitBreaker circuit;
    private ServiceDiscovery discovery;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.get().handler(this::convertPortfolioToEuro);
        router.post().handler(BodyHandler.create());
        router.post().handler(this::delegateWithCircuitBreaker);

        circuit = CircuitBreaker.create("circuit-breaker", vertx,
            new CircuitBreakerOptions()
                .setFallbackOnFailure(true)
                .setMaxFailures(3)
                .setResetTimeout(5000)
                .setTimeout(1000)
        );

        discovery = ServiceDiscovery.create(vertx);


        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080);
    }

    private void delegateWithCircuitBreaker(RoutingContext rc) {
        HttpEndpoint.rxGetWebClient(discovery, svc -> svc.getName().equals("currency-3rdparty-service"))
            .flatMap(client ->

                // TODO
                // Use the circuit breaker (circuit) to call the service. Use the rxExecuteCommandWithFallback` method.
                // This methods takes 2 parameters: the first one if a function taking a `Future` as parameter and
                // needs to report the success or failure on this future. The second method is a function providing
                // the fallback result. You must provide a JSON object as response. For the fallback use:
                // new JsonObject()
                //      .put("amount", rc.getBodyAsJson().getDouble("amount"))
                //      .put("currency", "USD"))
                // In the first function, use the given client, emit a POST request on / containing the incoming
                // payload (rc.getBodyAsJson()). Extract the response payload as JSON (bodyAsJsonObject). Don't
                // forget to subscribe (you can use subscribe(toObserver(fut)). You can have a look to the `delegate`
                // method as example.
                // -----
                circuit.rxExecuteCommandWithFallback(
                    fut ->
                        client.post("/").rxSendJsonObject(rc.getBodyAsJson())
                            .map(HttpResponse::bodyAsJsonObject)
                            .subscribe(toObserver(fut)),
                    err -> new JsonObject()
                        .put("amount", rc.getBodyAsJson().getDouble("amount"))
                        .put("currency", "USD")))

            // ----
            .map(JsonObject::toBuffer)
            .map(Buffer::new)

            .subscribe(toObserver(rc));
    }

    /**
     * Example of method not using a circuit breaker.
     *
     * @param rc the routing context
     */
    private void delegate(RoutingContext rc) {
        HttpEndpoint.rxGetWebClient(discovery, svc -> svc.getName().equals("currency-3rdparty-service"))
            .flatMap(client -> client.post("/").rxSendJsonObject(rc.getBodyAsJson()))
            .map(HttpResponse::bodyAsBuffer)
            .subscribe(toObserver(rc));
    }


    /**
     * Method to check the proxy requesting to convert the current portfolio to EUR.
     *
     * @param rc the routing context
     */
    private void convertPortfolioToEuro(RoutingContext rc) {
        EventBusService.getServiceProxy(discovery, svc -> svc.getName().equals("portfolio"), PortfolioService.class,
            ar -> {
                if (ar.failed()) {
                    rc.fail(ar.cause());
                } else {
                    ar.result().evaluate(res -> {
                        if (res.failed()) {
                            rc.fail(res.cause());
                        } else {
                            JsonObject payload = new JsonObject().put("amount", res.result()).put("currency", "EUR");
                            rc.setBody(new Buffer(payload.toBuffer()));
                            delegateWithCircuitBreaker(rc);
                        }
                    });
                }
            });
    }
}
