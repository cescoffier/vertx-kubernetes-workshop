package io.vertx.workshop.portfolio.kotlin

import io.vertx.workshop.portfolio.Portfolio

/**
 * A function providing a DSL for building [io.vertx.workshop.portfolio.Portfolio] objects.
 *
 * Structure representing a portfolio. It stores the available cash and the owned shares.
 *
 * @param cash  Sets the available cash. Method used by the converter.
 * @param shares  Sets the owned shares. Method used by the converter.
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.workshop.portfolio.Portfolio original] using Vert.x codegen.
 */
fun Portfolio(
  cash: Double? = null,
  shares: Map<String, Int>? = null): Portfolio = io.vertx.workshop.portfolio.Portfolio().apply {

  if (cash != null) {
    this.setCash(cash)
  }
  if (shares != null) {
    this.setShares(shares)
  }
}

