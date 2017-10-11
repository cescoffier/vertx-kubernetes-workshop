package io.vertx.workshop.currency;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Future;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Helpers {

    /**
     * Utility method to report the completion/failure from a Single to a Future.
     *
     * @param future the future
     * @return the single observer to pass to {@link Single#subscribe()}
     */
    public static SingleObserver<JsonObject> toObserver(Future<JsonObject> future) {
        return new SingleObserver<JsonObject>() {
            public void onSubscribe(@NonNull Disposable d) {
            }

            public void onSuccess(@NonNull JsonObject item) {
                future.tryComplete(item);
            }

            public void onError(Throwable error) {
                future.tryFail(error);
            }
        };
    }

    /**
     * Utility method to report the completion/failure from a Single to a Routing Context.
     *
     * @param rc the routing context
     * @return the single observer to pass to {@link Single#subscribe()}
     */
    public static SingleObserver<Buffer> toObserver(RoutingContext rc) {
        return new SingleObserver<Buffer>() {
            public void onSubscribe(@NonNull Disposable d) {
            }

            public void onSuccess(@NonNull Buffer payload) {
                rc.response().end(payload);
            }

            public void onError(Throwable error) {
                rc.fail(error);
            }
        };
    }

}
