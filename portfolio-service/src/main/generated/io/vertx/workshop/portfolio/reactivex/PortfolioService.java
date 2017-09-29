/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.workshop.portfolio.reactivex;

import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.vertx.core.json.JsonObject;
import io.vertx.core.AsyncResult;
import io.vertx.workshop.portfolio.Portfolio;
import io.vertx.core.Handler;

/**
 * A service managing a portfolio.
 * <p>
 * This service is an event bus service (a.k.a service proxies, or async RPC). The client and server are generated at
 * compile time.
 * <p>
 * All method are asynchronous and so ends with a  parameter.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.workshop.portfolio.PortfolioService original} non RX-ified interface using Vert.x codegen.
 */

@io.vertx.lang.reactivex.RxGen(io.vertx.workshop.portfolio.PortfolioService.class)
public class PortfolioService {

  public static final io.vertx.lang.reactivex.TypeArg<PortfolioService> __TYPE_ARG = new io.vertx.lang.reactivex.TypeArg<>(
    obj -> new PortfolioService((io.vertx.workshop.portfolio.PortfolioService) obj),
    PortfolioService::getDelegate
  );

  private final io.vertx.workshop.portfolio.PortfolioService delegate;
  
  public PortfolioService(io.vertx.workshop.portfolio.PortfolioService delegate) {
    this.delegate = delegate;
  }

  public io.vertx.workshop.portfolio.PortfolioService getDelegate() {
    return delegate;
  }

  /**
   * Gets the portfolio.
   * @param resultHandler the result handler called when the portfolio has been retrieved. The async result indicates whether the call was successful or not.
   */
  public void getPortfolio(Handler<AsyncResult<Portfolio>> resultHandler) { 
    delegate.getPortfolio(resultHandler);
  }

  /**
   * Gets the portfolio.
   * @return 
   */
  public Single<Portfolio> rxGetPortfolio() { 
    return new io.vertx.reactivex.core.impl.AsyncResultSingle<Portfolio>(handler -> {
      getPortfolio(handler);
    });
  }

  /**
   * Buy `amount` shares of the given shares (quote).
   * @param amount the amount
   * @param quote the last quote
   * @param resultHandler the result handler with the updated portfolio. If the action cannot be executed, the async result is market as a failure (not enough money, not enough shares available...)
   */
  public void buy(int amount, JsonObject quote, Handler<AsyncResult<Portfolio>> resultHandler) { 
    delegate.buy(amount, quote, resultHandler);
  }

  /**
   * Buy `amount` shares of the given shares (quote).
   * @param amount the amount
   * @param quote the last quote
   * @return 
   */
  public Single<Portfolio> rxBuy(int amount, JsonObject quote) { 
    return new io.vertx.reactivex.core.impl.AsyncResultSingle<Portfolio>(handler -> {
      buy(amount, quote, handler);
    });
  }

  /**
   * Sell `amount` shares of the given shares (quote).
   * @param amount the amount
   * @param quote the last quote
   * @param resultHandler the result handler with the updated portfolio. If the action cannot be executed, the async result is market as a failure (not enough share...)
   */
  public void sell(int amount, JsonObject quote, Handler<AsyncResult<Portfolio>> resultHandler) { 
    delegate.sell(amount, quote, resultHandler);
  }

  /**
   * Sell `amount` shares of the given shares (quote).
   * @param amount the amount
   * @param quote the last quote
   * @return 
   */
  public Single<Portfolio> rxSell(int amount, JsonObject quote) { 
    return new io.vertx.reactivex.core.impl.AsyncResultSingle<Portfolio>(handler -> {
      sell(amount, quote, handler);
    });
  }

  /**
   * Evaluates the current value of the portfolio.
   * @param resultHandler the result handler with the valuation
   */
  public void evaluate(Handler<AsyncResult<Double>> resultHandler) { 
    delegate.evaluate(resultHandler);
  }

  /**
   * Evaluates the current value of the portfolio.
   * @return 
   */
  public Single<Double> rxEvaluate() { 
    return new io.vertx.reactivex.core.impl.AsyncResultSingle<Double>(handler -> {
      evaluate(handler);
    });
  }


  public static  PortfolioService newInstance(io.vertx.workshop.portfolio.PortfolioService arg) {
    return arg != null ? new PortfolioService(arg) : null;
  }
}
