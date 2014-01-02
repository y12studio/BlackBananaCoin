package org.blackbananacoin.incubator.extsupernode;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.google.bitcoin.core.Utils;

public class BkbcUtils {

	public static final long BTC = 100000000L;
	
	public static final int mBTC = 100000;
	public static final int uBTC = 100;
	public static final BigInteger COIN = new BigInteger("100000000", 10);
	public static final BigInteger mCOIN = new BigInteger("100000", 10);
	public static final BigInteger uCOIN = new BigInteger("100", 10);

	public static String toBtcStr(BigInteger value) {
		checkNotNull(value);
		String r = "";
		BigDecimal v = new BigDecimal(value);
		if (value.compareTo(Utils.COIN) > 0) {
			BigDecimal valueInBTC = v.divide(new BigDecimal(Utils.COIN));
			r = valueInBTC.toPlainString() + "BTC";
		} else if (value.compareTo(mCOIN) > 0) {
			BigDecimal valueInmBTC = v.divide(new BigDecimal(mCOIN));
			r = valueInmBTC.toPlainString() + "mBTC";
		} else {
			BigDecimal valueInuBTC = v.divide(new BigDecimal(uCOIN));
			r = valueInuBTC.toPlainString() + "uBTC";
		}
		return r;
	}

	public static String toBtcStr(long value) {
		return toBtcStr(BigInteger.valueOf(value));
	}
}
