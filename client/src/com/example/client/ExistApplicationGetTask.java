package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import beans.CountryApplication;
import com.example.client.exceptions.XMLgenerationException;
import com.googlecode.openbeans.XMLDecoder;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

public class ExistApplicationGetTask extends AsyncTask<String, Integer, CountryApplication> {
	public ExistApplicationGetTask(String login, String password, URL serverURL) {
		this.login     = login;
		this.password  = password;
		this.serverURL = serverURL;
	}

	@Override
	protected CountryApplication doInBackground(String... data) {
		try {
			Client cl = new Client(serverURL);
			String requestXML = generateXML();
			String answerXML  = cl.execute(requestXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			return (CountryApplication) decoder.readObject();

		} catch (XMLgenerationException e) {
			Log.d("ANL", "XML generation error!");
		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "Server ApplicationConstrainTask IOException error!");
		}

		return new CountryApplication();
	}

	@Override
	protected void onPostExecute(CountryApplication result) {
		//TODO: обработка со стороны вьюшки
	}

	private String generateXML() throws XMLgenerationException{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer     = new StringWriter();

		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "application-request");
			serializer.attribute("", "login"   , login);
			serializer.attribute("", "password", password);
			serializer.endTag("", "application-request");

			serializer.endDocument();
			return writer.toString();

		} catch (IOException e) {
			throw new XMLgenerationException();
		}
	}

	private final String login;
	private final String password;
	private final URL    serverURL;
}
