package com.example.client;

import android.util.Log;

import java.net.*;
import java.io.*;

/**
 * Main class for work with server.
 * @author Podkopaev Anton
 */
public class Client {
	/**
	 * Constructs {@link Client} object that can operate with server.
	 * @param serverURL URL of server.
	 */
	public Client(URL serverURL) {
		this.serverURL = serverURL;
	}

	/**
	 * Sends to server requestStr string and returns server answer string.
	 * @param requestStr String that need to be sent to server.
	 * @return Server answer.
	 */
	public String execute(String requestStr) throws IOException {
		Log.d("ANL", "0");

		URLConnection connection = serverURL.openConnection();
		Log.d("ANL", "0");
		connection.setDoOutput(true);
		BufferedWriter out = new BufferedWriter(
				new OutputStreamWriter(connection.getOutputStream()));
		out.write(requestStr);
		out.close();

		Log.d("ANL", "1");
		InputStream stream = connection.getInputStream();
		Log.d("ANL", "11");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(stream));

		Log.d("ANL", "15");

		String inputLine;
		StringBuilder result = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			result.append("\n" + inputLine);
		}
		in.close();

		Log.d("ANL", "2");

		String resultString = result.toString();
		if (resultString.length() > 0) {
			return resultString.substring(1); //removing the last "\n"
		}
		return resultString;
	}

	private URL serverURL;
}