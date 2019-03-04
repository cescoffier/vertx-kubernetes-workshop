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


/**
 A service managing a portfolio.
 <p>
 This service is an event bus service (a.k.a service proxies, or async RPC). The client and server are generated at
 compile time.
 <p>
 @class
*/
export default class PortfolioService {

  constructor (eb: any, address: string);

  getPortfolio(resultHandler: (err: any, result: any) => any) : void;

  buy(amount: number, quote: Object, resultHandler: (err: any, result: any) => any) : void;

  sell(amount: number, quote: Object, resultHandler: (err: any, result: any) => any) : void;

  evaluate(resultHandler: (err: any, result: any) => any) : void;
}