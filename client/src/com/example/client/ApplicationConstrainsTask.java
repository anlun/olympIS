package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import com.example.client.exceptions.XmlGenerationException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

//TODO: завести отдельный класс для результата
//Что-то типа Application constrains
public class ApplicationConstrainsTask extends AsyncTask<String, Integer, String> {
	public ApplicationConstrainsTask(String country, String login, String password, URL serverURL) {
		this.country     = country;
		this.login       = login;
		this.password    = password;
		this.serverURL   = serverURL;
	}

	@Override
	public String doInBackground(String... data) {
		try {
			Client cl = new Client(serverURL);
			String requestXML = generateXML();
			String answerXML  = cl.execute(requestXML);

			Log.d("ANL", answerXML);

			//country = getCountryName(answerXML);

		} catch (XmlGenerationException e) {
			Log.d("ANL", "XML generation error!");
			country = null;
		}

		return "";
	}

	protected String generateXML() throws XmlGenerationException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer     = new StringWriter();

		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "application-constrains-request");
			serializer.attribute("", "country", country);
			serializer.attribute("", "login"   , login);
			serializer.attribute("", "password", password);
			serializer.endTag("", "application-constrains-request");

			serializer.endDocument();
			return writer.toString();

		} catch (IOException e) {
			throw new XmlGenerationException();
		}
	}

	private String country;
	private String login;
	private String password;
	private URL    serverURL;
}
