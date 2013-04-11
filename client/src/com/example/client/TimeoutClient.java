package com.example.client;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

/**
 * @author Podkopaev Anton
 */
public class TimeoutClient {
	/**
	 */
	public TimeoutClient(int timeout, URL serverURL) {
		this.client  = new Client(serverURL);
		this.timeout = timeout;
	}

	public TimeoutClient(URL serverURL) {
		this(standardTimeout, serverURL);
	}

	/**
	 */
	public String execute(final String requestStr) throws IOException, TimeoutException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String>  future = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return client.execute(requestStr);
			}
		});

		try {
			return future.get(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new IOException();
		} catch (ExecutionException e) {
			throw new IOException();
		}
	}

	private Client client;
	private int    timeout; //in seconds

	private static final int standardTimeout = 10;
}
