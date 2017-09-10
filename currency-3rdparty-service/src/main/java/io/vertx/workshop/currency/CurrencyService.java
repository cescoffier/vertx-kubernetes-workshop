package io.vertx.workshop.currency;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Random;

/**
 * @author Someone you don't want to know...
 */
public class CurrencyService extends AbstractVerticle {

    private Random random = new Random();

    @Override
    public void start(Future<Void> future) throws Exception {
        Router router = Router.router(vertx);
        router.get().handler(rc -> rc.response().end("OK"));
        router.post().handler(BodyHandler.create());
        router.post().handler(this::handle);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(config().getInteger("port", 8080),
                ar -> future.handle(ar.mapEmpty()));
    }

    private void handle(RoutingContext rc) {
        @Nullable JsonObject json = rc.getBodyAsJson();
        if (json == null || json.getDouble("amount") == null) {
            System.out.println("No content or no amount");
            rc.fail(400);
            return;
        }

        double amount = json.getDouble("amount");
        String target = json.getString("currency");
        if (target == null) {
            target = "EUR";
        }
        double rate = getRate(target);
        if (rate == -1) {
            System.out.println("Unknown currency: " + target);
            rc.fail(400);
        }
        
        int i = random.nextInt(10);
        if (i < 5) {

            rc.response().end(new JsonObject()
                .put("amount", convert(amount, rate))
                .put("currency", target).encode()
            );
        } else if (i < 8) {
            // Failure
            rc.fail(500);
        }
        // Timeout, we don't write the response.
    }

    private double getRate(String target) {
        if (target.equalsIgnoreCase("USD")) {
            return 1;
        }
        if (target.equalsIgnoreCase("EUR")) {
            return 0.84;
        }
        if (target.equalsIgnoreCase("GBP")) {
            return 0.77;
        }
        return -1;
    }

    private double convert(double amount, double rate) {
        return amount * rate;
    }
}
