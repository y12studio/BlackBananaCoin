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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Key;

public class CurMix {

	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new GsonFactory();
	static String EXURL_BLOCKCHAIN = "https://blockchain.info/ticker";
	static String EXURL_YAHOO_USDTWD = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%22http%3A%2F%2Ffinance.yahoo.com%2Fd%2Fquotes.csv%3Fe%3D.csv%26f%3Dc4l1%26s%3DUSDTWD%3DX%22%3B&format=json&diagnostics=true&callback=";

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
		GenericUrl urlBlockchain = new GenericUrl(EXURL_BLOCKCHAIN);
		GenericUrl urlYahooTwd = new GenericUrl(EXURL_YAHOO_USDTWD);
		HttpRequest requestBlockChain = requestFactory.buildGetRequest(urlBlockchain);
		GenericJson json = requestBlockChain.execute().parseAs(GenericJson.class);
		System.out.println(json);
		Map ntdJson = (Map) json.get("USD");
		System.out.println(ntdJson.put("buy", 119.001));
		json.put("NTD", ntdJson);
		System.out.println(json);

		
		HttpRequest requestYahoo = requestFactory.buildGetRequest(urlYahooTwd);
		GenericJson jsonYahoo = requestYahoo.execute().parseAs(GenericJson.class);
		System.out.println(jsonYahoo);
		Map query = (Map) jsonYahoo.get("query");
		Map results = (Map) query.get("results");
		Map row = (Map) results.get("row");
		double twd = Double.parseDouble((String)row.get("col1"));
		System.out.println(twd);
		
	}

}
