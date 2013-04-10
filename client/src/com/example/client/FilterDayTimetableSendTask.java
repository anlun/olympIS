package com.example.client;


import android.os.AsyncTask;
import android.util.Log;
import beans.*;
import com.googlecode.openbeans.XMLDecoder;
import utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

public class FilterDayTimetableSendTask extends AsyncTask<String, Integer, Boolean> {
	public FilterDayTimetableSendTask(ArrayList<Filter> filters, int dayNumber, URL serverURL, CalendarActivity calendarActivity) {
		this.filters   = filters;
		this.dayNumber = dayNumber;
		this.serverURL = serverURL;
		this.calendarActivity = calendarActivity;
	}

	@Override
	public Boolean doInBackground(String... data) {
		try {
			Client cl = new Client(serverURL);
			FilterListForTimetable fl = new FilterListForTimetable(filters, dayNumber);
			String requestXML = Utils.encoderWrap(fl.serialize());
			String answerXML  = cl.execute(requestXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			dayTimetable = (DayTimetable) decoder.readObject();

			return dayTimetable != null;

		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "FilterSendTask IOException error!");
		}

		return false;
	}

	@Override
	public void onPostExecute(Boolean result) {
		calendarActivity.onFilterDayTimetableSendTask(dayTimetable, dayNumber);
	}

	private ArrayList<Filter> filters;
	private int               dayNumber;
	private DayTimetable      dayTimetable;
	private URL               serverURL;
	private CalendarActivity calendarActivity;
}