Build supernode on Ubuntu 13.10
-------------------------------

sudo apt-get install openjdk-7-jdk
sudo apt-get install maven
sudo apt-get install protobuf-compiler
$ protoc --version
libprotoc 2.4.1
git clone https://github.com/bitsofproof/supernode.git
cd supernode
$ update-alternatives --display mvn
export M2_HOME=/usr/share/maven
export JAVA_HOME=/usr/lib/jvm/java-1.7.0-openjdk-amd64
$ mvn --version
Apache Maven 3.0.4
Maven home: /usr/share/maven
Java version: 1.7.0_25, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-7-openjdk-amd64/jre
Default locale: zh_TW, platform encoding: UTF-8
OS name: "linux", version: "3.11.0-12-generic", arch: "amd64", family: "unix"

$ mvn package
$ java -server -jar server/target/bitsofproof-server-1.3.7-SNAPSHOT.jar testnet3 memdb
2013-12-31 14:42:03,524 [INFO] Main main bitsofproof supernode (c) 2013 bits of proof zrt.
2013-12-31 14:42:03,826 [INFO] Main main Loading profile: testnet3
2013-12-31 14:42:03,826 [INFO] Main main Loading profile: memdb
2013-12-31 14:42:05,425 [INFO] CachedBlockStore main Reset block store
2013-12-31 14:42:07,243 [INFO] BitcoinPeer Peer-thread-0 Connection to '/Satoshi:0.8.3/' [70001] at testnet-seed.bitcoin.petertodd.org/62.213.207.209:18333 Open connections: 1
2013-12-31 14:42:08,236 [INFO] BitcoinPeer Peer-thread-0 Connection to '/Satoshi:0.8.5/' [70001] at testnet-seed.bitcoin.petertodd.org/162.243.63.90:18333 Open connections: 2
2013-12-31 14:42:09,893 [INFO] BitcoinPeer Peer-thread-0 Connection to '/Satoshi:0.8.99/' [70001] at testnet-seed.bitcoin.petertodd.org/162.243.249.127:18333 Open connections: 4
2013-12-31 14:42:09,894 [INFO] BitcoinPeer Peer-thread-0 Connection to '/Satoshi:0.8.99/' [70001] at testnet-seed.bitcoin.petertodd.org/62.210.203.13:18333 Open connections: 4
2013-12-31 14:42:11,465 [INFO] BitcoinPeer Peer-thread-0 Connection to '/Satoshi:0.8.1/' [70001] at testnet-seed.bitcoin.petertodd.org/141.138.205.129:18333 Open connections: 7
2013-12-31 14:42:11,854 [INFO] BitcoinPeer Peer-thread-1 Connection to '/Satoshi:0.8.5/' [70001] at testnet-seed.bitcoin.petertodd.org/81.230.10.247:18333 Open connections: 8
2013-12-31 14:42:12,143 [INFO] BitcoinPeer Peer-thread-1 Connection to '/Satoshi:0.8.5/' [70001] at testnet-seed.bitcoin.petertodd.org/65.111.166.213:18333 Open connections: 10
2013-12-31 14:42:12,484 [INFO] BitcoinPeer Peer-thread-1 Connection to '/Satoshi:0.8.99/' [70001] at testnet-seed.bitcoin.petertodd.org/150.162.202.3:18333 Open connections: 10
2013-12-31 14:42:12,654 [INFO] BitcoinPeer Peer-thread-1 Connection to '/Satoshi:0.8.1/' [70001] at testnet-seed.bitcoin.petertodd.org/162.243.61.132:18333 Open connections: 10
2013-12-31 14:42:12,834 [INFO] BitcoinPeer Peer-thread-0 Connection to '/Satoshi:0.8.2.2/' [70001] at testnet-seed.bitcoin.petertodd.org/213.232.95.7:18333 Open connections: 10


$ cd ~
$ git clone https://github.com/bitsofproof/bop-api-example.git
$ cd bop-api-example/
$ java -jar target/bop-api-example-1.2.jar -s SERVER -u USER -p PASSWORD

Bop client - MQ? - BOP Server




// APIServerInABox.java
com.bitsofproof.supernode.testbox.APIServerInABox.java
mineBlock



Windows7 Build
--------------
git clone
install JCE Unlimited Strength Policy Jurisdiction at win7 jdk (google)
install protobuf at C:\y12\tools\protoc-2.4.1-win32\protoc.exe

cd supernode
mvn package
cd server/target
java -server -Xmx2g -jar bitsofproof-server-x.x.x.jar testnet3 memdb



