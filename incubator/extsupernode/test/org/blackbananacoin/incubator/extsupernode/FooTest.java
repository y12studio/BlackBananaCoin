package org.blackbananacoin.incubator.extsupernode;

import java.math.BigInteger;

public class FooTest {

	public static void main(String[] args) {		
		System.out.println(BkbcUtils.toBtcStr(12345678L));
		System.out.println(BkbcUtils.toBtcStr(12345L));
		System.out.println(BkbcUtils.toBtcStr(BkbcUtils.mCOIN.multiply(BigInteger.valueOf(1230L))));
		System.out.println(BkbcUtils.toBtcStr(BkbcUtils.uCOIN.multiply(BigInteger.valueOf(3210L))));
	}

}
