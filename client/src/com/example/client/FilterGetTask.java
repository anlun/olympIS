package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import beans.Filter;
import com.example.client.exceptions.XMLgenerationException;
import com.googlecode.openbeans.XMLDecoder;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

public class FilterGetTask extends AsyncTask<String, Integer, Boolean> {
	public FilterGetTask(URL serverURL, CalendarActivity calendarActivity) {
		this.serverURL  = serverURL;
		this.filterList = null;
		this.calendarActivity = calendarActivity;
	}

	@Override
	public Boolean doInBackground(String... data) {
		try {
			Client cl = new Client(serverURL);

			String requestXML = generateXML();
			String answerXML  = cl.execute(requestXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			Log.d("ANL", answerXML);
			filterList = (ArrayList<Filter>) decoder.readObject();
			return filterList != null;

		} catch (XMLgenerationException e) {
			Log.d("ANL", "FilterGetTask XMLgenerationException error!");
		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "FilterGetTask IOException error!");
		} catch (Exception e) {
			Log.d("ANL", e.toString());
		}

		return false;
	}

	@Override
	public void onPostExecute(Boolean result) {
		Log.d("ANL", "FilterGetTask.onPostExecute");
		if (result) {
			calendarActivity.onFilterGetTask(filterList);
		}
	}

	protected String generateXML() throws XMLgenerationException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer     = new StringWriter();

		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "filters-request");
			serializer.endTag  ("", "filters-request");

			serializer.endDocument();
			return writer.toString();

		} catch (IOException e) {
			throw new XMLgenerationException();
		}
	}


	private ArrayList<Filter> filterList;
	private URL               serverURL;
	private CalendarActivity  calendarActivity;
}

