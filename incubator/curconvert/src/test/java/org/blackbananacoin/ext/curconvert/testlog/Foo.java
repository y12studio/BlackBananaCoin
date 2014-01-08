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

import java.util.Date;

import org.blackbananacoin.ext.curconvert.TwdBit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Foo {

	private int num;
	private Date time;
	public int getNum() {
		return num;
	}
	@Override
	public String toString() {
		return "Foo [num=" + num + ", time=" + time + "]";
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
		Foo foo = new Foo();
		foo.setNum(100);
		foo.setTime(new Date());
		
		String jsonStr = gson.toJson(foo);
		System.out.println(jsonStr);
		
		Foo foo2 = gson.fromJson(jsonStr, Foo.class);
		System.out.println(foo2);
		
		TwdBit twdBit = new TwdBit();
		twdBit.setTwdPerUsd(30.1);
		twdBit.setUsdPerBtc(800.3);
		twdBit.update();
		System.out.println(gson.toJson(twdBit));
		
	}
}
