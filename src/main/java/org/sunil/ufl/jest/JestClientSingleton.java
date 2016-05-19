package org.sunil.ufl.jest;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import org.apache.log4j.Logger;

/**
 * This is a singleton using which we can access the JestClient.
 * 
 * @author sunil
 *
 */
public class JestClientSingleton {
	private Logger LOG = Logger.getLogger(JestClientSingleton.class);

	private static JestClientSingleton instance;
	
	private JestClient client = null;
	
	private JestClientSingleton() {
		JestClientFactory factory = new JestClientFactory();
		String esServer = getElasticSearchServer();

		if (LOG.isInfoEnabled()) {
			LOG.info("Index - " + esServer);
		}

		factory.setHttpClientConfig(new HttpClientConfig.Builder(esServer).multiThreaded(true).readTimeout(100000).build());
		client = factory.getObject();

		if (LOG.isInfoEnabled()) {
			LOG.info("JestClient created");
		}
	}
	
	public static JestClientSingleton getInstance() {
		if (instance == null) {
			synchronized (JestClientSingleton.class) {
				if (instance == null) {
					instance = new JestClientSingleton();
				}
			}
		}
		
		return instance;
	}

	/**
	 * This will create a new JestClient and return the client.
	 * 
	 * @return JestClient
	 */
	public JestClient getJestClient() {
		return client;
	}

	public void shutdownClient() {
		try {
			if (client != null) {
				client.shutdownClient();
				if (LOG.isInfoEnabled()) {
					LOG.info("JestClient shutdown");
				}
			}
		} catch (Exception exception) {
			LOG.error("Error shutting down JestClient - " + exception.getMessage(), exception);
		}
	}

	private String getElasticSearchServer() {
		String host = "http://ec2-52-25-42-245.us-west-2.compute.amazonaws.com";
		String port = "7450";

		return host + ":" + port;
	}
}
