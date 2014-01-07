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
package org.blackbananacoin.incubator.extbitcoinj;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerAddress;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.params.TestNet3Params;
import static com.google.common.base.Preconditions.*;

/**
 * @author user TP's TestNet Faucet
 */
public class ReadBuildTestnetCoin extends MyFooWallet {

	public static final byte[] localhost = { 127, 0, 0, 1 };
	final int port = 19993;
	private static PeerAddress myAddr;
	public static final String filePrefix = "WhatNameThisLocalFile";

	public static final NetworkParameters param = TestNet3Params.get();
	
	public ReadBuildTestnetCoin(PeerAddress... addresses) {
		super(filePrefix, param, addresses);
	}

	@Override
	protected void postCheckKeyOk() {
		checkNotNull(getWallet());
		BigInteger v = getWallet().getBalance();
		System.out.println("Wallet Balance = " + v);
		System.out.println("Convert to "
				+ Utils.bitcoinValueToFriendlyString(v) + " BTC");
	}

	public void haha() throws UnknownHostException {
		myAddr = new PeerAddress(InetAddress.getByAddress(localhost), port, 0);
	}

	public static void main(String[] args) {
		new ReadBuildTestnetCoin(null);
	}

}
