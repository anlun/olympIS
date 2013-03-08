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
	 * @param serverUrl URL of server.
	 */
	public Client(URL serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * Sends to server requestStr string and returns server answer string.
	 * @param requestStr String that need to be sent to server.
	 * @return Server answer.
	 */
	public String execute(String requestStr) {
		try {
			URLConnection connection = serverUrl.openConnection();
			connection.setDoOutput(true);
			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()));
			out.write(requestStr);
			out.close();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			String inputLine;
			StringBuilder result = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				result.append("\n" + inputLine);
			}
			in.close();

			String resultString = result.toString();
			if (resultString.length() > 0) {
				return resultString.substring(1); //removing the last "\n"
			}
			return resultString;

		} catch (IOException e) {
			return "Fail in connect to server.";
		}
	}

	private URL serverUrl;
}