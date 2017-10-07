package io.vertx.workshop.trader.impl;

import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.workshop.portfolio.PortfolioService;

import java.util.Map;
import java.util.Random;

/**
 * A small utility class to initialize the compulsive traders and implement the stupid trading logic.
 */
public class TraderUtils {

    private final static Random RANDOM = new Random();

    public static String pickACompany() {
        int choice = RANDOM.nextInt(2);
        switch (choice) {
            case 0:
                return "Divinator";
            case 1:
                return "MacroHard";
            default:
                return "Black Coat";
        }
    }

    public static boolean timeToSell() {
        return RANDOM.nextBoolean();
    }

    public static int pickANumber() {
        return RANDOM.nextInt(6) + 1;
    }

    public static void dumbTradingLogic(String company, int numberOfShares, PortfolioService portfolio, JsonObject quote) {
        if (quote.getString("name").equals(company)) {
            if (TraderUtils.timeToSell()) {
                portfolio.sell(numberOfShares, quote, p -> {
                    if (p.succeeded()) {
                        System.out.println("Sold " + numberOfShares + " of " + company + "!");
                    } else {
                        System.out.println("D'oh, failed to sell " + numberOfShares + " of " + company + " : " + p.cause());
                    }
                });
            } else {
                portfolio.buy(numberOfShares, quote, p -> {
                    if (p.succeeded()) {
                        System.out.println("Bought " + numberOfShares + " of " + company + " !");
                    } else {
                        System.out.println("D'oh, failed to buy " + numberOfShares + " of " + company + " : " + p.cause());
                    }
                });
            }
        }
    }

    public static Completable dumbTradingLogic(String company, int numberOfShares, io.vertx.workshop.portfolio.reactivex.PortfolioService portfolio, JsonObject quote) {
        if (quote.getString("name").equals(company)) {
            if (TraderUtils.timeToSell()) {
                System.out.println("Trying to sell " + numberOfShares + " " + company);
                return portfolio.rxSell(numberOfShares, quote)
                    .doOnSuccess(p -> System.out.println("Sold " + numberOfShares + " of " + company + "!"))
                    .doOnError(e -> System.out.println("D'oh, failed to sell " + numberOfShares + " of "
                        + company + ": " + e.getMessage()))
                    .toCompletable();
            } else {
                System.out.println("Trying to buy " + numberOfShares + " " + company);
                return portfolio.rxBuy(numberOfShares, quote)
                    .doOnSuccess(p -> System.out.println("Bought " + numberOfShares + " of " + company + " !"))
                    .doOnError(e -> System.out.println("D'oh, failed to buy " + numberOfShares + " of " + company + " : " + e
                        .getMessage()))
                    .toCompletable();
            }
        }

        return Completable.complete();
    }

    /**
     * Version called from Groovy where json are maps.
     *
     * @param company        the company
     * @param numberOfShares the number of share to buy or sell
     * @param portfolio      the portfolio service
     * @param quote          the quote
     */
    public static void dumbTradingLogic(String company, int numberOfShares,
                                        PortfolioService portfolio, Map<String, Object> quote) {
        JsonObject json = new JsonObject(quote);
        dumbTradingLogic(company, numberOfShares, portfolio, json);
    }
}
