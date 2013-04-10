package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import beans.ApplicationConstrain;
import com.example.client.exceptions.XMLgenerationException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import com.googlecode.openbeans.XMLDecoder;

/**
 * Class for getting country {@link ApplicationConstrain}.
 * @author Podkopaev Anton
 */
public class ApplicationConstrainTask extends AsyncTask<String, Integer, Boolean> {
	public ApplicationConstrainTask(String country, String login, String password, URL serverURL) {
		this.country     = country;
		this.login       = login;
		this.password    = password;
		this.serverURL   = serverURL;
	}

	@Override
	public Boolean doInBackground(String... data) {
		try {
			TimeoutClient cl = new TimeoutClient(serverURL);
			String requestXML = generateXML();
			String answerXML  = cl.execute(requestXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			applicationConstrain = (ApplicationConstrain) decoder.readObject();
			return true;

		} catch (XMLgenerationException e) {
			Log.d("ANL", "XML generation error!");
		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "Server ApplicationConstrainTask IOException error!");
		} catch (TimeoutException e) {
			Log.d("ANL", "ApplicationConstrainTask timeout!");
		}

		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		//TODO: some activity with ApplicationConstrain
		if (result) {
			Log.d("ANL", "Application constrain request succeeded.");
		} else {
			Log.d("ANL", "Application constrain request failed!");
		}
	}

	private String generateXML() throws XMLgenerationException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer     = new StringWriter();

		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "application-constrain-request");
			serializer.attribute("", "country" , country);
			serializer.attribute("", "login"   , login);
			serializer.attribute("", "password", password);
			serializer.endTag("", "application-constrain-request");

			serializer.endDocument();
			return writer.toString();

		} catch (IOException e) {
			throw new XMLgenerationException();
		}
	}

	private String country;
	private String login;
	private String password;
	private URL    serverURL;

	private ApplicationConstrain applicationConstrain;
}
