package com.example.client;

import java.net.*;
import java.io.*;

public class Client {
	public Client(URL serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String execute(String cmdStr) {
		try {
			URLConnection connection = serverUrl.openConnection();
			connection.setDoOutput(true);
			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()));
			out.write(cmdStr);
			out.close();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			String inputLine;
			StringBuilder result = new StringBuilder();
			while ((inputLine = in.readLine()) != null)
				result.append("\n" + inputLine);
			in.close();

			String resultString = result.toString();
			if (resultString.length() > 0)
				return resultString.substring(1); //removing "\n"
			return resultString;

		} catch (IOException e) {
			return "Fail in connect to server.";
		}
	}

	private URL serverUrl;
}