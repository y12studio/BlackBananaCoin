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

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.List;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.PeerAddress;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.kits.WalletAppKit;
import com.google.bitcoin.params.TestNet3Params;

/**
 * @author user
 * 
 */
public class MyFooWallet {

	public static final String ADDR_tpfaucet_appspot_com = "mmhmMNfBiZZ37g1tgg2t8DDbNoEdqKVxAL";

	private Wallet wallet;

	private WalletAppKit kit;

	private NetworkParameters param;

	public MyFooWallet() {
		setParam(TestNet3Params.get());
		final String FILE_PREFIX = "localfile-testnet3";
		kit = new WalletAppKit(getParam(), new File("."), FILE_PREFIX) {
			@Override
			protected void onSetupCompleted() {
				onSetupCompletd();
			}
		};
		// Download the block chain and wait until it's done.
		getKit().startAndWait();
	}

	private void onSetupCompletd() {
		System.out.println("Wallet SetupCompleted");
		setWallet(getKit().wallet());
		addNewKeyIfNotExist();
	}

	public MyFooWallet(String anotherFilePrefix, NetworkParameters param,
			PeerAddress... addresses) {
		this.setParam(param);
		// String filePrefix = "Another_ID_For_Test_Wallet";
		getKit().setPeerNodes(addresses);
		// Download the block chain and wait until it's done.
		kit = new WalletAppKit(param, new File("."), anotherFilePrefix) {
			@Override
			protected void onSetupCompleted() {
				onSetupCompletd();
			}
		};

		getKit().startAndWait();
	}

	public void addNewKeyIfNotExist() {

		checkNotNull(getKit(), "Kit null!");
		checkNotNull(getWallet(), "Wallet null!");

		List<ECKey> klist = getWallet().getKeys();
		System.out.println("Wallet key size : " + klist.size());
		// only add one key to wallet
		checkElementIndex(0, klist.size());
		ECKey eckey = klist.get(0);
		checkNotNull(eckey, "ECKey null!");

		getKit().wallet().addKey(eckey);
		printKeyInfo(eckey);
		postCheckKeyOk();
	}

	private void printKeyInfo(ECKey key) {
		checkNotNull(key);
		checkNotNull(getParam(), "NetworkParam null!");

		System.out
				.println("[NOTE] keep the localfile-testnet3.wallet in a safe place for send testnet3's bitcoin back.");
		System.out.println("testnet3 bitcoin address = "
				+ key.toAddress(getParam()));
		System.out.println("WalletClient Import PrivateKeyStr = "
				+ key.getPrivateKeyEncoded(getParam()).toString());
	}

	protected void postCheckKeyOk() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "MyFooWallet [wallet=" + wallet + ", kit=" + kit + ", param="
				+ param + "]";
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

	public NetworkParameters getParam() {
		return this.param;
	}

	public void setParam(NetworkParameters param) {
		this.param = param;
	}

}
