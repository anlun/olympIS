package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

public class LoginTask extends AsyncTask<String, Integer, Boolean> {
	public LoginTask(String login, String password, URL serverURL, AuthorizationActivity authorizationActivityObject) {
		this.login       = login;
		this.password    = password;
		this.serverURL   = serverURL;
		this.countryName = null;
        this.authorizationActivityObject = authorizationActivityObject;
	}

	@Override
	protected Boolean doInBackground(String... data) {
		try {
			Client cl = new Client(serverURL);
			String requestXML = generateXML();
			String answerXML  = cl.execute(requestXML);

			Log.d("ANL", answerXML);

			countryName = getCountryName(answerXML);

		} catch (XmlGenerationException e) {
			Log.d("ANL", "XML generation error!");
			countryName = null;
		}

		return countryName != null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		//TODO
		if (result) {
			Log.d("ANL", "Login by " + countryName + " was successfully done.");
		} else {
			Log.d("ANL", "Login fail!");
		}

        authorizationActivityObject.onLogin(result);
	}

	protected String generateXML() throws XmlGenerationException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter  writer     = new StringWriter();

		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "login-request");
			serializer.attribute("", "login"   , login);
			serializer.attribute("", "password", password);
			serializer.endTag("", "login-request");

			serializer.endDocument();
			return writer.toString();

		} catch (IOException e) {
			throw new XmlGenerationException();
		}
	}

	protected String getCountryName(String xmlResponse) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new StringReader(xmlResponse));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String name = parser.getName();
					if (name.equalsIgnoreCase("login-response")) {
						String countryName = parser.getAttributeValue("", "country");
						if (countryName != "") {
							return countryName;
						} else {
							return null;
						}
					}
				}
				eventType = parser.next();
			}

		} catch (XmlPullParserException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return null;
	}

	protected class XmlGenerationException extends Exception {
	}

	private String login;
	private String password;
	private URL serverURL;
	private String countryName;
    AuthorizationActivity authorizationActivityObject;
}
