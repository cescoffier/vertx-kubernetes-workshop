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

package io.vertx.workshop.portfolio.scala

import io.vertx.lang.scala.HandlerOps._
import scala.reflect.runtime.universe._
import io.vertx.lang.scala.Converter._
import io.vertx.workshop.portfolio.{PortfolioService => JPortfolioService}
import io.vertx.lang.scala.AsyncResultWrapper
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.workshop.portfolio.{Portfolio => JPortfolio}

/**
  * A service managing a portfolio.
  * 
  * This service is an event bus service (a.k.a service proxies, or async RPC). The client and server are generated at
  * compile time.
  * 
  * All method are asynchronous and so ends with a  parameter.
  */
class PortfolioService(private val _asJava: Object) {

  def asJava = _asJava

  /**
    * Gets the portfolio.
    * @param resultHandler the result handler called when the portfolio has been retrieved. The async result indicates whether the call was successful or not.
    */
  def getPortfolio(resultHandler: Handler[AsyncResult[Portfolio]]): Unit = {
    asJava.asInstanceOf[JPortfolioService].getPortfolio({x: AsyncResult[JPortfolio] => resultHandler.handle(AsyncResultWrapper[JPortfolio, Portfolio](x, a => Portfolio(a)))})
  }

  /**
    * Buy `amount` shares of the given shares (quote).
    * @param amount the amount
    * @param quote the last quote
    * @param resultHandler the result handler with the updated portfolio. If the action cannot be executed, the async result is market as a failure (not enough money, not enough shares available...)
    */
  def buy(amount: Int, quote: io.vertx.core.json.JsonObject, resultHandler: Handler[AsyncResult[Portfolio]]): Unit = {
    asJava.asInstanceOf[JPortfolioService].buy(amount.asInstanceOf[java.lang.Integer], quote, {x: AsyncResult[JPortfolio] => resultHandler.handle(AsyncResultWrapper[JPortfolio, Portfolio](x, a => Portfolio(a)))})
  }

  /**
    * Sell `amount` shares of the given shares (quote).
    * @param amount the amount
    * @param quote the last quote
    * @param resultHandler the result handler with the updated portfolio. If the action cannot be executed, the async result is market as a failure (not enough share...)
    */
  def sell(amount: Int, quote: io.vertx.core.json.JsonObject, resultHandler: Handler[AsyncResult[Portfolio]]): Unit = {
    asJava.asInstanceOf[JPortfolioService].sell(amount.asInstanceOf[java.lang.Integer], quote, {x: AsyncResult[JPortfolio] => resultHandler.handle(AsyncResultWrapper[JPortfolio, Portfolio](x, a => Portfolio(a)))})
  }

  /**
    * Evaluates the current value of the portfolio.
    * @param resultHandler the result handler with the valuation
    */
  def evaluate(resultHandler: Handler[AsyncResult[Double]]): Unit = {
    asJava.asInstanceOf[JPortfolioService].evaluate({x: AsyncResult[java.lang.Double] => resultHandler.handle(AsyncResultWrapper[java.lang.Double, Double](x, a => a.asInstanceOf[Double]))})
  }

 /**
   * Like [[getPortfolio]] but returns a [[scala.concurrent.Future]] instead of taking an AsyncResultHandler.
   */
  def getPortfolioFuture(): scala.concurrent.Future[Portfolio] = {
    val promiseAndHandler = handlerForAsyncResultWithConversion[JPortfolio, Portfolio](x => Portfolio(x))
    asJava.asInstanceOf[JPortfolioService].getPortfolio(promiseAndHandler._1)
    promiseAndHandler._2.future
  }

 /**
   * Like [[buy]] but returns a [[scala.concurrent.Future]] instead of taking an AsyncResultHandler.
   */
  def buyFuture(amount: Int, quote: io.vertx.core.json.JsonObject): scala.concurrent.Future[Portfolio] = {
    val promiseAndHandler = handlerForAsyncResultWithConversion[JPortfolio, Portfolio](x => Portfolio(x))
    asJava.asInstanceOf[JPortfolioService].buy(amount.asInstanceOf[java.lang.Integer], quote, promiseAndHandler._1)
    promiseAndHandler._2.future
  }

 /**
   * Like [[sell]] but returns a [[scala.concurrent.Future]] instead of taking an AsyncResultHandler.
   */
  def sellFuture(amount: Int, quote: io.vertx.core.json.JsonObject): scala.concurrent.Future[Portfolio] = {
    val promiseAndHandler = handlerForAsyncResultWithConversion[JPortfolio, Portfolio](x => Portfolio(x))
    asJava.asInstanceOf[JPortfolioService].sell(amount.asInstanceOf[java.lang.Integer], quote, promiseAndHandler._1)
    promiseAndHandler._2.future
  }

 /**
   * Like [[evaluate]] but returns a [[scala.concurrent.Future]] instead of taking an AsyncResultHandler.
   */
  def evaluateFuture(): scala.concurrent.Future[Double] = {
    val promiseAndHandler = handlerForAsyncResultWithConversion[java.lang.Double, Double](x => x.asInstanceOf[Double])
    asJava.asInstanceOf[JPortfolioService].evaluate(promiseAndHandler._1)
    promiseAndHandler._2.future
  }

}

object PortfolioService {
  def apply(asJava: JPortfolioService) = new PortfolioService(asJava)  
}
