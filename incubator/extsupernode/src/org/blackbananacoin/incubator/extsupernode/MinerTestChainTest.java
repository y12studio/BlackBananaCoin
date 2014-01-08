package org.blackbananacoin.incubator.extsupernode;


import java.io.IOException;

import com.bitsofproof.supernode.api.JMSServerConnector;
import com.bitsofproof.supernode.common.InMemoryBusConnectionFactory;
import com.bitsofproof.supernode.common.ValidationException;
import com.bitsofproof.supernode.core.BitcoinNetwork;
import com.bitsofproof.supernode.core.Chain;
import com.bitsofproof.supernode.core.FixedAddressDiscovery;
import com.bitsofproof.supernode.core.ImplementBCSAPI;
import com.bitsofproof.supernode.core.TxHandler;
import com.bitsofproof.supernode.model.LvlMemoryStore;
import com.bitsofproof.supernode.model.LvlStore;

public class MinerTestChainTest {
	
	private BitcoinNetwork createNetwork(Chain chain) throws IOException {
		FixedAddressDiscovery discovery = new FixedAddressDiscovery();
		discovery.setConnectTo("localhost");
		LvlStore store = new LvlStore();
		store.setStore(new LvlMemoryStore());
		BitcoinNetwork network = new BitcoinNetwork(0);
		network.setChain(chain);
		network.setStore(store);
		network.setListen(false);
		network.setDiscovery(discovery);
		return network;
	}

	public void testGetFoo() throws IOException, ValidationException {
		Chain chain = new MinerTestChain();
		BitcoinNetwork network = createNetwork(chain);
		JMSServerConnector apiconnector = new JMSServerConnector();
		InMemoryBusConnectionFactory connectionFactory = new InMemoryBusConnectionFactory();
		apiconnector.setConnectionFactory(connectionFactory);
		TxHandler txhandler = new TxHandler(network);
		
		ImplementBCSAPI bcsapi = new ImplementBCSAPI(network, txhandler);
		bcsapi.setConnectionFactory(connectionFactory);
		
		network.getStore().resetStore(chain);
		network.getStore().cache(chain, 0);
		txhandler.clear();
		
		bcsapi.init();
		apiconnector.init();
		
		System.out.println(network.getNumberOfConnections());
		
	}
	
	public static void main(String[] args) throws IOException, ValidationException {
		MinerTestChainTest t = new MinerTestChainTest();
		t.testGetFoo();
	}

}
