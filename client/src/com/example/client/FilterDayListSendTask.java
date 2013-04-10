package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import beans.DayList;
import beans.Filter;
import beans.FilterListForDayList;
import com.googlecode.openbeans.XMLDecoder;
import utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

public class FilterDayListSendTask extends AsyncTask<String, Integer, Boolean> {
	public FilterDayListSendTask(ArrayList<Filter> filters, URL serverURL, CalendarActivity calendarActivity) {
		this.filters    = filters;
		this.serverURL = serverURL;
		this.calendarActivity = calendarActivity;
	}

	@Override
	public Boolean doInBackground(String... data) {
		try {
			Log.d("ANL", "Client cl = new Client(serverURL);");
			Client cl = new Client(serverURL);

			Log.d("ANL", "String requestXML = Utils.beanToString(new FilterListForDayList(filters));");
			FilterListForDayList fl = new FilterListForDayList(filters);
			String requestXML = Utils.encoderWrap(fl.serialize());
			Log.d("ANL", "String answerXML  = cl.execute(requestXML);");
			Log.d("ANL", requestXML);
			String answerXML  = cl.execute(requestXML);

			Log.d("ANL", "XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes(\"UTF-8\")));");
			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			Log.d("ANL", "dayList = (DayList) decoder.readObject();");
			dayList = (DayList) decoder.readObject();

			Log.d("ANL", "return dayList != null;");
			return dayList != null;

		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "FilterSendTask IOException error!");
		}

		return false;
	}

	@Override
	public void onPostExecute(Boolean result) {
		calendarActivity.onFilterDayListSendTask(dayList.getListOfDays());
	}

	private ArrayList<Filter> filters;
	private DayList           dayList;
	private URL               serverURL;
	private CalendarActivity calendarActivity;
}
