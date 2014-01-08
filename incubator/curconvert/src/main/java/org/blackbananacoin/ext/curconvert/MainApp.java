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
package org.blackbananacoin.ext.curconvert;

import static com.google.common.base.Preconditions.checkElementIndex;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.blackbananacoin.ext.curconvert.ExtRateGenServiceImpl.GenType;
import org.blackbananacoin.ext.curconvert.ExtRateGenServiceImpl.MyCallback;

import com.google.common.io.Files;

public class MainApp {

	public static void main(String[] args) {
		String dir = ".";
		if(args!=null){
			checkElementIndex(0, args.length);
			dir = args[0];
		}
		final File dirFile = new File(dir);
		if(!dirFile.exists()){
			dirFile.mkdir();
		}
		System.out.println(dirFile.getAbsolutePath());
		final File blockchainFile = new File(dirFile,"blockchain_twd.json");
		final File twdbtcFile = new File(dirFile,"twdbtc.json");
		System.out.println(blockchainFile.getAbsolutePath());
		System.out.println(twdbtcFile.getAbsolutePath());
		final Charset charset = Charset.forName("UTF-8");
		String currencyCode = ExtRateGenServiceImpl.CODE_TWD; // New Taiwan
																// Dollar
		final ExtRateGenServiceImpl app = new ExtRateGenServiceImpl(
				currencyCode);
		try {

			app.startRequest(new MyCallback() {
				public void onResult(Map<GenType, String> jsonStrMap) {
					
					try {
						Files.write(jsonStrMap.get(GenType.BLOCKCHAIN), blockchainFile, charset);
						Files.write(jsonStrMap.get(GenType.TWDUSD), twdbtcFile, charset);
						System.out.println("=====Blockchain======");
						System.out.println(jsonStrMap.get(GenType.BLOCKCHAIN));					
						System.out.println("=====TwdUsd======");
						System.out.println(jsonStrMap.get(GenType.TWDUSD));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
