/*
 * Copyright 2013 Y12STUDIO
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.blackbananacoin.ext.curconvert.testlog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class CurFutureMixChain {

	private ListeningExecutorService pool = MoreExecutors
			.listeningDecorator(Executors.newFixedThreadPool(2));
	
	public static final String YAHOO_CUR_TAG = "TWD";

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new GsonFactory();
	public static String EXURL_BLOCKCHAIN = "https://blockchain.info/ticker";
	public static String EXURL_YAHOO_USDTWD = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%22http%3A%2F%2Ffinance.yahoo.com%2Fd%2Fquotes.csv%3Fe%3D.csv%26f%3Dc4l1%26s%3DUSD"
			+ YAHOO_CUR_TAG
			+ "%3DX%22%3B&format=json&diagnostics=true&callback=";

	public static void main(String[] args) throws Exception {
		new CurFutureMixChain().run();
	}

	public void run() throws Exception {

		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(new HttpRequestInitializer() {
					public void initialize(HttpRequest request) {
						request.setParser(new JsonObjectParser(JSON_FACTORY));
					}
				});
		final GenericUrl urlBlockchain = new GenericUrl(EXURL_BLOCKCHAIN);
		final GenericUrl urlYahooTwd = new GenericUrl(EXURL_YAHOO_USDTWD);
		final HttpRequest requestBlockChain = requestFactory
				.buildGetRequest(urlBlockchain);

		final HttpRequest requestYahoo = requestFactory
				.buildGetRequest(urlYahooTwd);

		final ListenableFuture<Double> futureYahoo = pool
				.submit(new Callable<Double>() {
					public Double call() throws Exception {
						GenericJson jsonYahoo = requestYahoo.execute().parseAs(
								GenericJson.class);
						System.out.println(jsonYahoo);
						Map query = (Map) jsonYahoo.get("query");
						Map results = (Map) query.get("results");
						Map row = (Map) results.get("row");
						double twd = Double.parseDouble((String) row
								.get("col1"));
						System.out
								.println("======== GET TWD/USD Rate =========");
						System.out.println(twd);
						return twd;
					}
				});

		final AsyncFunction<Double, GenericJson> relevanceAsyncFun = new AsyncFunction<Double, GenericJson>() {
			public ListenableFuture<GenericJson> apply(final Double twdPerUsd)
					throws Exception {

				final ListenableFuture<GenericJson> futureReqBlockChain = pool
						.submit(new Callable<GenericJson>() {
							public GenericJson call() throws Exception {
								GenericJson json = requestBlockChain.execute()
										.parseAs(GenericJson.class);
								handleTwdAppend(twdPerUsd, json);
								return json;
							}

							private void handleTwdAppend(
									final Double twdPerUsd, GenericJson json) {
								System.out.println(json);
								Map usdJson = (Map) json.get("USD");
								Map ntdJson = new HashMap<String,Object>();
								BigDecimal usdBuy = (BigDecimal) usdJson
										.get("buy");
								BigDecimal usdLast = (BigDecimal) usdJson
										.get("last");
								BigDecimal usd15m = (BigDecimal) usdJson
										.get("15m");
								BigDecimal usdSell = (BigDecimal) usdJson
										.get("sell");
								BigDecimal twdBuy = usdBuy.multiply(BigDecimal
										.valueOf(twdPerUsd));
								BigDecimal twdSell = usdSell
										.multiply(BigDecimal.valueOf(twdPerUsd));
								BigDecimal twd15m = usd15m.multiply(BigDecimal
										.valueOf(twdPerUsd));
								BigDecimal twdLast = usdLast
										.multiply(BigDecimal.valueOf(twdPerUsd));
								ntdJson.put("buy", twdBuy);
								ntdJson.put("sell", twdSell);
								ntdJson.put("last", twdLast);
								ntdJson.put("15m", twd15m);
								ntdJson.put("symbol", "NT$");
								json.put(YAHOO_CUR_TAG, ntdJson);
								// System.out.println(json);
							}
						});

				return futureReqBlockChain;
			}
		};

		ListenableFuture<GenericJson> futureMix = Futures.transform(
				futureYahoo, relevanceAsyncFun);

		Futures.addCallback(futureMix, new FutureCallback<GenericJson>() {

			public void onSuccess(GenericJson result) {
				System.out.println("======== RESULT =========");
				System.out.println(result);
				pool.shutdown();
			}

			public void onFailure(Throwable t) {
				t.printStackTrace();
				pool.shutdown();
			}
		});

	}

}
