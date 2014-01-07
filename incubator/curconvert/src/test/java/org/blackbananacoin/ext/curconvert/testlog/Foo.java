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
