package br.gov.ce.fortaleza.config;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;



public class RestEasyHttps {
	public static HttpClient createSSLHttpClient() throws Exception {
		SSLContext ctx = SSLContext.getInstance("TLS");
		TrustManager[] trustManagers = getTrustManagers();
		//KeyManager[] keyManagers = getKeyManagers(); // Key managers are only required for two-way SSL, or client
														// certificate authentication.

		ctx.init(null, trustManagers, new SecureRandom()); // pass null for keyManagers if you are not doing
																	// client certificate authentication (i.e doing
																	// one-way SSL,)
		SSLSocketFactory factory = new SSLSocketFactory(ctx);

		HttpClient client = new DefaultHttpClient();
		ClientConnectionManager manager = client.getConnectionManager();
		manager.getSchemeRegistry().register(new Scheme("https", factory, 8443));

		return client;
	}

	public static KeyStore loadTrustStore() throws Exception {
		KeyStore trustStore = KeyStore.getInstance("jks");
		trustStore.load(new FileInputStream(new File("truststore.jks")), "123456".toCharArray());
		return trustStore;
	}

	protected static TrustManager[] getTrustManagers() throws Exception {
		KeyStore trustStore = loadTrustStore();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);
		return tmf.getTrustManagers();
	}

//	// Key managers are only required for two-way SSL, or client certificate
//	// authentication.
//	public static KeyStore loadKeyStore() throws Exception {
//		KeyStore keyStore = KeyStore.getInstance("jks");
//		keyStore.load(new FileInputStream(new File("/path/to/keystore")), "password".toCharArray());
//		return keyStore;
//	}
//
//	// Key managers are only required for two-way SSL, or client certificate
//	// authentication.
//	protected static KeyManager[] getKeyManagers() throws Exception {
//		KeyStore keyStore = loadKeyStore();
//		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//		kmf.init(keyStore, "password".toCharArray());
//		return kmf.getKeyManagers();
//	}

}
