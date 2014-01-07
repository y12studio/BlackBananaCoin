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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Key;

public class CurJsonTypeParse {

	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new GsonFactory();
	static String EXURL_BLOCKCHAIN = "https://blockchain.info/ticker";

	public static class ExFeed {

		@Key("USD")
		public BlockChainTicker usd;

		@Key("CNY")
		public BlockChainTicker cny;

		@Key("EUR")
		public BlockChainTicker eur;
	}

	public static class BlockChainTicker {
		@Override
		public String toString() {
			return "BlockChainCurrency [price15m=" + price15m + ", priceLast="
					+ priceLast + ", priceBuy=" + priceBuy + ", priceSell="
					+ priceSell + "]";
		}

		@Key("15m")
		public double price15m;

		@Key("last")
		public double priceLast;

		@Key("buy")
		public double priceBuy;

		@Key("sell")
		public double priceSell;
	}

	public static class ExUrl extends GenericUrl {
		public ExUrl(String encodedUrl) {
			super(encodedUrl);
		}
	}

	public static void main(String[] args) {

		try {
			try {
				run();
				return;
			} catch (HttpResponseException e) {
				System.err.println(e.getMessage());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(1);

	}

	private static void run() throws Exception {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT
				.createRequestFactory(new HttpRequestInitializer() {
					public void initialize(HttpRequest request) {
						request.setParser(new JsonObjectParser(JSON_FACTORY));
					}
				});

		ExUrl url = new ExUrl(EXURL_BLOCKCHAIN);
		HttpRequest request = requestFactory.buildGetRequest(url);
		ExFeed exFeed = request.execute().parseAs(ExFeed.class);
		System.out.println("USD=" + exFeed.usd);
		System.out.println("CNY=" + exFeed.cny);
		System.out.println("EUR=" + exFeed.eur);

	}

}
