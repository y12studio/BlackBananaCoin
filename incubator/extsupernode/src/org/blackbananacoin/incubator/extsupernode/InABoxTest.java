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
package org.blackbananacoin.incubator.extsupernode;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bitsofproof.supernode.api.Address;
import com.bitsofproof.supernode.api.BCSAPI;
import com.bitsofproof.supernode.api.BCSAPIException;
import com.bitsofproof.supernode.api.Block;
import com.bitsofproof.supernode.api.Transaction;
import com.bitsofproof.supernode.api.TrunkListener;
import com.bitsofproof.supernode.common.ECKeyPair;
import com.bitsofproof.supernode.common.ValidationException;
import com.bitsofproof.supernode.wallet.AccountListener;
import com.bitsofproof.supernode.wallet.AccountManager;
import com.bitsofproof.supernode.wallet.AddressListAccountManager;
import com.bitsofproof.supernode.wallet.KeyListAccountManager;

public class InABoxTest {

	private static final Logger log = LoggerFactory.getLogger(InABoxTest.class);
	private int sendBtc = 6;
	private static SuperNodeApiTester box;
	private static BCSAPI api;

	public void init() throws IOException, ValidationException,
			BCSAPIException, InterruptedException {
		box = new SuperNodeApiTester(new MinerTestChain());
		api = box.getApi();
		run();
	}

	public void run() throws BCSAPIException, InterruptedException {
		checkNotNull(api);
		final Address target = ECKeyPair.createNew(true).getAddress();
		log.debug("Target account address = " + target);
		AddressListAccountManager targetAccount = new AddressListAccountManager();
		targetAccount.addAddress(target);
		api.registerTransactionListener(targetAccount);

		ECKeyPair miner = ECKeyPair.createNew(true);
		Address a = miner.getAddress();
		log.debug("Miner account address = " + a);
		box.setNewCoinsAddress(a);
		final KeyListAccountManager minerAccount = new KeyListAccountManager();
		minerAccount.addKey(miner);
		api.registerTransactionListener(minerAccount);

		final Semaphore blockMined = new Semaphore(0);
		api.registerTrunkListener(new TrunkListener() {
			@Override
			public void trunkUpdate(List<Block> removed, List<Block> added) {
				blockMined.release();
			}
		});

		final Semaphore minerAccountChanged = new Semaphore(0);
		minerAccount.addAccountListener(new AccountListener() {
			@Override
			public void accountChanged(AccountManager account, Transaction t) {
				minerAccountChanged.release();
			}
		});
		final Semaphore targetAccountChanged = new Semaphore(0);
		targetAccount.addAccountListener(new AccountListener() {
			@Override
			public void accountChanged(AccountManager account, Transaction t) {
				targetAccountChanged.release();
			}
		});

		// make 10 blocks
		box.mine(10, 2, 1000);

		for (int i = 0; i < 10; ++i) {
			log.info("[Pay Target : {} ]", i);
			try {
				blockMined.acquireUninterruptibly();
				minerAccountChanged.acquireUninterruptibly();
				if (i > 0) {
					minerAccountChanged.acquireUninterruptibly();
					targetAccountChanged.acquireUninterruptibly();
				}

				log.info("[MinerAccount balance={}, SEND Target " + sendBtc
						+ " BTC]",
						BkbcUtils.toBtcStr(minerAccount.getBalance()));
				Transaction t = minerAccount.pay(target, sendBtc
						* BkbcUtils.BTC, 0, true);
				api.sendTransaction(t);
				targetAccountChanged.acquireUninterruptibly();
				minerAccountChanged.acquireUninterruptibly();
				log.info("[MinerAccount balance={}]",
						BkbcUtils.toBtcStr(minerAccount.getBalance()));
				log.info("[TargetAccount balance={}]",
						BkbcUtils.toBtcStr(targetAccount.getBalance()));
			} catch (ValidationException | BCSAPIException e) {
				log.error("REJECT ", e);
			}
		}
		log.debug("miner balance="
				+ BkbcUtils.toBtcStr(minerAccount.getBalance()));
		log.debug("target balance="
				+ BkbcUtils.toBtcStr(targetAccount.getBalance()));
		box.showStatResult();
	}

	public static void main(String[] args) throws IOException,
			ValidationException, BCSAPIException, InterruptedException {
		InABoxTest st = new InABoxTest();
		st.init();
	}

}
