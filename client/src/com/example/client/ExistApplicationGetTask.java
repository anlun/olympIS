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
import java.util.concurrent.TimeoutException;

public class ExistApplicationGetTask extends AsyncTask<String, Integer, CountryApplication> {
	public ExistApplicationGetTask(String login, String password, URL serverURL, CountryGUI countryGUIObject) {
		this.login     = login;
		this.password  = password;
		this.serverURL = serverURL;
		this.countryGUIObject = countryGUIObject;
	}

	@Override
	protected CountryApplication doInBackground(String... data) {
		try {
			TimeoutClient cl  = new TimeoutClient(serverURL);
			String requestXML = generateXML();
			String answerXML  = cl.execute(requestXML);
			Log.d("DAN", "requestXML" + requestXML);
			Log.d("DAN", "answerXML" + answerXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			return (CountryApplication) decoder.readObject();

		} catch (XMLgenerationException e) {
			Log.d("ANL", "XML generation error!");
		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "Server ExistApplicationGetTask IOException error!");
		} catch (TimeoutException e) {
			Log.d("ANL", "ExistApplicationGetTask timeout!");
		}

		return new CountryApplication();
	}

	@Override
	protected void onPostExecute(CountryApplication result) {
		countryGUIObject.getCountryApplicationFromServer(result);
	}

	private String generateXML() throws XMLgenerationException{
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter  writer     = new StringWriter();

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
	private CountryGUI countryGUIObject;
}
