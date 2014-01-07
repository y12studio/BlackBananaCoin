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

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class CurFutureMix {

	private ListeningExecutorService pool = MoreExecutors
			.listeningDecorator(Executors.newFixedThreadPool(2));

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new GsonFactory();
	public static String EXURL_BLOCKCHAIN = "https://blockchain.info/ticker";
	public static String EXURL_YAHOO_USDTWD = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%22http%3A%2F%2Ffinance.yahoo.com%2Fd%2Fquotes.csv%3Fe%3D.csv%26f%3Dc4l1%26s%3DUSDTWD%3DX%22%3B&format=json&diagnostics=true&callback=";

	public static void main(String[] args) throws Exception {
		new CurFutureMix().run();
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

		final ListenableFuture<GenericJson> futureReqBlockChain = pool
				.submit(new Callable<GenericJson>() {
					public GenericJson call() throws Exception {
						GenericJson json = requestBlockChain.execute().parseAs(
								GenericJson.class);
						return json;
					}
				});

		Futures.addCallback(futureReqBlockChain,
				new FutureCallback<GenericJson>() {

					public void onSuccess(GenericJson result) {
						GenericJson json = result;
						System.out.println(json);
						Map ntdJson = (Map) json.get("USD");
						System.out.println(ntdJson.put("buy", 119.001));
						json.put("NTD", ntdJson);
						System.out.println(json);
					}

					public void onFailure(Throwable t) {
						// TODO Auto-generated method stub

					}
				});

		final HttpRequest requestYahoo = requestFactory
				.buildGetRequest(urlYahooTwd);
		final ListenableFuture<GenericJson> futureYahoo = pool
				.submit(new Callable<GenericJson>() {
					public GenericJson call() throws Exception {
						GenericJson json = requestYahoo.execute().parseAs(
								GenericJson.class);
						return json;
					}
				});

		Futures.addCallback(futureYahoo, new FutureCallback<GenericJson>() {

			public void onSuccess(GenericJson result) {
				GenericJson jsonYahoo = result;
				System.out.println(jsonYahoo);
				Map query = (Map) jsonYahoo.get("query");
				Map results = (Map) query.get("results");
				Map row = (Map) results.get("row");
				double twd = Double.parseDouble((String) row.get("col1"));
				System.out.println(twd);
			}

			public void onFailure(Throwable t) {
				// TODO Auto-generated method stub

			}
		});

	}

}
