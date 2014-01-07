


Cron Tab
--------

$ which java
/usr/bin/java
$ java -version
java version "1.6.0_07"
Java(TM) SE Runtime Environment (build 1.6.0_07-b06)
Java HotSpot(TM) 64-Bit Server VM (build 10.0-b23, mixed mode)


crontab -e

*/10 * * * * /usr/bin/java -jar /home/dir_where_jar/curconvert-0.x.x.jar /home/dir_for_jsonfile/ 1>&2 &>/dev/null

crontab -l


Test Log
--------

[Setup - google-http-java-client - Download and Setup instructions - Google HTTP Client Library for Java - Google Project Hosting](https://code.google.com/p/google-http-java-client/wiki/Setup)


On Android, you will need to explicitly exclude unused dependencies:

    <dependency>
      <groupId>com.google.http-client</groupId>
      <artifactId>google-http-client</artifactId>
      <version>1.17.0-rc</version>
      <exclusions>
        <exclusion>
          <artifactId>xpp3</artifactId>
          <groupId>xpp3</groupId>
        </exclusion>
        <exclusion>
          <artifactId>httpclient</artifactId>
          <groupId>org.apache.httpcomponents</groupId>
        </exclusion>
        <exclusion>
          <artifactId>junit</artifactId>
          <groupId>junit</groupId>
        </exclusion>
        <exclusion>
          <artifactId>android</artifactId>
          <groupId>com.google.android</groupId>
        </exclusion>
      </exclusions>
    </dependency>




[koush/AndroidAsync](https://github.com/koush/AndroidAsync)

pom.xml

  <dependencies>
  	<dependency>
  		<groupId>com.google.guava</groupId>
  		<artifactId>guava</artifactId>
  		<version>15.0</version>
  	</dependency>
  	<dependency>
  		<groupId>com.koushikdutta.async</groupId>
  		<artifactId>androidasync</artifactId>
  		<version>1.2.1</version>
  	</dependency>
  	<dependency>
  		<groupId>org.apache.httpcomponents</groupId>
  		<artifactId>httpclient</artifactId>
  		<version>4.3.1</version>
  	</dependency>
  </dependencies>


Exception in thread "main" java.lang.NoClassDefFoundError: android/os/Looper
	at com.koushikdutta.async.http.AsyncHttpRequest.<init>(AsyncHttpRequest.java:129)
	at com.koushikdutta.async.http.AsyncHttpRequest.<init>(AsyncHttpRequest.java:93)
	at com.koushikdutta.async.http.AsyncHttpGet.<init>(AsyncHttpGet.java:9)
	at com.koushikdutta.async.http.AsyncHttpClient.getString(AsyncHttpClient.java:436)
	at org.blackbananacoin.ext.curconvert.CurMain.main(CurMain.java:15)
Caused by: java.lang.ClassNotFoundException: android.os.Looper
	at java.net.URLClassLoader$1.run(Unknown Source)
	at java.net.URLClassLoader$1.run(Unknown Source)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.net.URLClassLoader.findClass(Unknown Source)
	at java.lang.ClassLoader.loadClass(Unknown Source)
	at sun.misc.Launcher$AppClassLoader.loadClass(Unknown Source)
	at java.lang.ClassLoader.loadClass(Unknown Source)
	... 5 more