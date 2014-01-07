package org.blackbananacoin.incubator.extbitcoinj;

import java.math.BigInteger;
import java.util.Date;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.Range;

import static com.google.common.collect.DiscreteDomain.integers;

public class Foo {
	

	public static void main(String[] args) {
		// why isn't the maximum target 2^256 - 1
		BigInteger unitTestTarget = BigInteger.valueOf (1).shiftLeft (256).subtract (BigInteger.ONE);
		System.out.println(unitTestTarget);
		System.out.println(unitTestTarget.toString(16));

		BigInteger testnet3Target = BigInteger.valueOf (0xFFFFL).shiftLeft (8 * (0x1d - 3));
		System.out.println(testnet3Target);
		System.out.println(testnet3Target.toString(16));
		
		ContiguousSet<Integer> set = ContiguousSet.create(Range.closed(Integer.MIN_VALUE, Integer.MAX_VALUE), integers());
		System.out.println(new Date());
		int count = 0;
		for(Integer v : set){
			count++;
		}
		System.out.println(new Date());		
		
	}

}
