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

import static com.google.common.collect.DiscreteDomain.integers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bitsofproof.supernode.api.Address;
import com.bitsofproof.supernode.api.BCSAPIException;
import com.bitsofproof.supernode.api.Block;
import com.bitsofproof.supernode.api.JMSServerConnector;
import com.bitsofproof.supernode.api.Transaction;
import com.bitsofproof.supernode.common.Hash;
import com.bitsofproof.supernode.common.InMemoryBusConnectionFactory;
import com.bitsofproof.supernode.common.ValidationException;
import com.bitsofproof.supernode.core.BitcoinNetwork;
import com.bitsofproof.supernode.core.Chain;
import com.bitsofproof.supernode.core.Difficulty;
import com.bitsofproof.supernode.core.FixedAddressDiscovery;
import com.bitsofproof.supernode.core.ImplementBCSAPI;
import com.bitsofproof.supernode.core.TxHandler;
import com.bitsofproof.supernode.core.TxListener;
import com.bitsofproof.supernode.model.LvlMemoryStore;
import com.bitsofproof.supernode.model.LvlStore;
import com.bitsofproof.supernode.model.Tx;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.Range;

public class SuperNodeApiTester {

	// Get a DescriptiveStatistics instance
	private DescriptiveStatistics statsMs = new DescriptiveStatistics();
	private DescriptiveStatistics statsCount = new DescriptiveStatistics();

	private ContiguousSet<Integer> nonceTrySet = ContiguousSet.create(
			Range.closed(Integer.MIN_VALUE, Integer.MAX_VALUE), integers());
	private static final Logger log = LoggerFactory
			.getLogger(SuperNodeApiTester.class);
	private final InMemoryBusConnectionFactory connectionFactory = new InMemoryBusConnectionFactory();

	private BitcoinNetwork network;
	private TxHandler txhandler;
	private JMSServerConnector api;

	private ImplementBCSAPI bcsapi;

	private final Chain chain;

	private Address newCoinsAddress;

	public SuperNodeApiTester(Chain theChain) {
		this.chain = theChain;
		try {
			init();
		} catch (IOException | ValidationException e) {
			e.printStackTrace();
		}
	}

	public void setNewCoinsAddress(Address a) {
		newCoinsAddress = a;
	}

	public void mineBlock(Block b) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		BigInteger target = Difficulty.getTarget(b.getDifficultyTarget());
		log.debug("[Mine] target=0x{}", target.toString(16));
		int count = 0;
		for (Integer nonce : nonceTrySet) {
			b.setNonce(nonce);
			b.computeHash();
			BigInteger hashAsInteger = new Hash(b.getHash()).toBigInteger();
			count++;
			if (hashAsInteger.compareTo(target) <= 0) {
				log.debug("[Mine End] count={}, nonce=0x{}", count,
						Integer.toHexString(nonce));
				log.debug("[Mine End] hashInteger=0x{}",
						hashAsInteger.toString(16));
				break;
			}
		}
		statsMs.addValue(stopwatch.elapsed(TimeUnit.MILLISECONDS));
		statsCount.addValue(count);
		log.debug("Mine Block with time {} and hash {}", stopwatch, b.getHash());
	}

	public void mine(final int nblocks, final int numberOfTxInBlock,
			final int timeout) {
		final BlockingQueue<Transaction> mempool = new LinkedBlockingQueue<Transaction>();
		final Set<String> seen = new HashSet<String>();

		Thread miner = new Thread(new Runnable() {
			public void run() {
				log.debug("miner run");
				txhandler.addTransactionListener(new TxListener() {
					public void process(Tx transaction, boolean doubleSpend) {
						synchronized (seen) {
							if (!transaction.getInputs().get(0).getSourceHash()
									.equals(Hash.ZERO_HASH_STRING)
									&& !seen.contains(transaction.getHash())) {
								Transaction t = Transaction
										.fromWireDump(transaction.toWireDump());
								mempool.offer(t);
								seen.add(transaction.getHash());
							}
						}
					}
				});
				String previousHash = chain.getGenesis().getHash();
				for (int blockHeight = 1; blockHeight <= nblocks; ++blockHeight) {
					Transaction coinbase = null;
					try {
						coinbase = Transaction.createCoinbase(newCoinsAddress,
								MineTestChain.REWARD_COINS * BkbcUtils.BTC,
								blockHeight);
						Block block = createBlock(previousHash, coinbase);

						if (blockHeight > 1) {
							mempool.drainTo(block.getTransactions());
							if (block.getTransactions().size() < numberOfTxInBlock) {
								try {
									Transaction t = mempool.poll(timeout,
											TimeUnit.MILLISECONDS);
									if (t != null) {
										block.getTransactions().add(t);
										mempool.drainTo(block.getTransactions());
									}
								} catch (InterruptedException e) {
								}
							}
						}

						mineBlock(block);
						previousHash = block.getHash();
						getApi().sendBlock(block);
					} catch (ValidationException | BCSAPIException e) {
						log.error("Server in a box ", e);
					}
				}
			}
		});
		miner.setName("Miner in the box");
		miner.setDaemon(true);
		miner.start();
	}

	public Block createBlock(String previous, Transaction coinbase) {
		Block block = new Block();
		try {
			Thread.sleep(1001);
		} catch (InterruptedException e) {
		}
		block.setCreateTime(System.currentTimeMillis() / 1000);
		block.setDifficultyTarget(chain.getGenesis().getDifficultyTarget());
		block.setPreviousHash(previous);
		block.setVersion(2);
		block.setNonce(0);
		block.setTransactions(new ArrayList<Transaction>());
		block.getTransactions().add(coinbase);
		return block;
	}

	public void init() throws IOException, ValidationException {
		LvlStore store = new LvlStore();
		store.setStore(new LvlMemoryStore());
		FixedAddressDiscovery discovery = new FixedAddressDiscovery();
		discovery.setConnectTo("localhost");
		network = new BitcoinNetwork(0);
		network.setChain(chain);
		network.setStore(store);
		network.setListen(false);
		network.setDiscovery(discovery);
		txhandler = new TxHandler(network);
		setApi(new JMSServerConnector());
		getApi().setConnectionFactory(connectionFactory);
		bcsapi = new ImplementBCSAPI(network, txhandler);
		bcsapi.setConnectionFactory(connectionFactory);
		reset();
		bcsapi.init();
		getApi().init();
	}

	public void reset() throws ValidationException {
		network.getStore().resetStore(chain);
		network.getStore().cache(chain, 0);
		txhandler.clear();
	}

	public static void main(String[] args) {
		System.out.println("hello");
	}

	public JMSServerConnector getApi() {
		return api;
	}

	public void setApi(JMSServerConnector api) {
		this.api = api;
	}

	public void showStatResult() {
		log.info("[Mine count] mean={},stddev={}", statsCount.getMean(),
				statsCount.getStandardDeviation());
		log.info("[Mine time] mean={}ms,median={}", statsMs.getMean(),
				statsMs.getPercentile(50));
		log.info("stdDev={}", statsMs.getStandardDeviation());

	}

}
