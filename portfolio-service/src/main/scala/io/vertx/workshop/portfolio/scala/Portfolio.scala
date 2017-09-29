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

import io.vertx.lang.scala.json.Json._
import io.vertx.core.json.JsonObject
import scala.collection.JavaConverters._
import io.vertx.workshop.portfolio.{Portfolio => JPortfolio}

/**
  * Structure representing a portfolio. It stores the available cash and the owned shares.
  */
class Portfolio(private val _asJava: JPortfolio) {

  def asJava = _asJava

  /**
    * Sets the available cash. Method used by the converter.
    */
  def setCash(value: Double) = {
    asJava.setCash(value)
    this
  }
  def getCash: Double = {
    asJava.getCash().asInstanceOf[Double]
  }

  /**
    * Sets the owned shares. Method used by the converter.
    */
  def setShares(value: Map[String, Int]) = {
    asJava.setShares(value.mapValues(Int.box).asJava)
    this
  }
  def getShares: scala.collection.mutable.Map[String, Int] = {
    collection.mutable.Map(asJava.getShares().asScala.mapValues(x => x.asInstanceOf[Int]).toSeq: _*)
  }
}

object Portfolio {
  
  def apply() = {
    new Portfolio(new JPortfolio(emptyObj()))
  }
  
  def apply(t: JPortfolio) = {
    if (t != null) {
      new Portfolio(t)
    } else {
      new Portfolio(new JPortfolio(emptyObj()))
    }
  }
  
  def fromJson(json: JsonObject): Portfolio = {
    if (json != null) {
      new Portfolio(new JPortfolio(json))
    } else {
      new Portfolio(new JPortfolio(emptyObj()))
    }
  }
}
