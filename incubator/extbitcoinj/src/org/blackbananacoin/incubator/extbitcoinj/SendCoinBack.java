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

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigInteger;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.core.WrongNetworkException;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author user TP's TestNet Faucet http://tpfaucet.appspot.com/
 *         https://code.google
 *         .com/p/bitcoinj/wiki/GettingStarted#Keys_and_addresses
 */
public class SendCoinBack extends MyFooWallet {

	@Override
	protected void postCheckKeyOk() {

		try {
			Address tpfaucetAddress;
			tpfaucetAddress = new Address(TN3PARAMS, ADDR_tpfaucet_appspot_com);
			Wallet w = getWallet();

			// send 100x min tx fee back.
			BigInteger value = Transaction.REFERENCE_DEFAULT_MIN_TX_FEE
					.multiply(BigInteger.valueOf(100));
			System.out.println("Sendback " + value + "="
					+ Utils.bitcoinValueToFriendlyString(value) + " BTC");
			// Now send the coins back! Send with a small fee attached to ensure
			// rapid confirmation.
			final BigInteger amountToSend = value
					.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
			System.out.println("Fee="
					+ Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
			final Wallet.SendResult sendResult = w.sendCoins(getKit()
					.peerGroup(), tpfaucetAddress, amountToSend);
			checkNotNull(sendResult); // We should never try to send more coins
										// than
										// we have!
			System.out.println("Sending ...");
			// Register a callback that is invoked when the transaction has
			// propagated across the network.
			// This shows a second style of registering ListenableFuture
			// callbacks,
			// it works when you don't
			// need access to the object the future returns.
			sendResult.broadcastComplete.addListener(new Runnable() {
				public void run() {
					// The wallet has changed now, it'll get auto saved shortly
					// or
					// when the app shuts down.
					System.out
							.println("Sent coins onwards! Transaction hash is "
									+ sendResult.tx.getHashAsString());
				}
			}, MoreExecutors.sameThreadExecutor());
			
		} catch (WrongNetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AddressFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new SendCoinBack();
	}

}
