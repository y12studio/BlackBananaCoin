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

import java.io.File;
import java.util.List;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.kits.WalletAppKit;
import com.google.bitcoin.params.TestNet3Params;

/**
 * @author user
 *
 */
public class MyFooWallet {

	public static final TestNet3Params TN3PARAMS = TestNet3Params.get();

	public static final String ADDR_tpfaucet_appspot_com = "mmhmMNfBiZZ37g1tgg2t8DDbNoEdqKVxAL";
	
	public static final String FILE_PREFIX = "localfile-testnet3";
	private Wallet wallet;

	private WalletAppKit kit = new WalletAppKit(TN3PARAMS, new File("."),
			FILE_PREFIX) {
		@Override
		protected void onSetupCompleted() {
			System.out.println("Wallet SetupCompleted");
			setWallet(getKit().wallet());
			addNewKeyIfNotExist();
		}
	};

	public MyFooWallet() {
		// Download the block chain and wait until it's done.
		getKit().startAndWait();
	}

	public void addNewKeyIfNotExist() {
		List<ECKey> klist = getWallet().getKeys();
		System.out.println("Wallet key size : " + klist.size());
		// only add one key to wallet
		ECKey eckey = klist.size() < 1 ? new ECKey() : klist.get(0);
		getKit().wallet().addKey(eckey);
		printKeyInfo(eckey);
		postCheckKeyOk();
	}

	private void printKeyInfo(ECKey key) {
		System.out.println("[NOTE] keep the localfile-testnet3.wallet in a safe place for send testnet3's bitcoin back.");
		System.out.println("testnet3 bitcoin address = "
				+ key.toAddress(TN3PARAMS));
		System.out.println("WalletClient Import PrivateKeyStr = "
				+ key.getPrivateKeyEncoded(TN3PARAMS).toString());
	}

	protected void postCheckKeyOk() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new MyFooWallet();
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public WalletAppKit getKit() {
		return kit;
	}

	public void setKit(WalletAppKit kit) {
		this.kit = kit;
	}

}
