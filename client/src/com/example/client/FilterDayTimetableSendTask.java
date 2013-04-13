package com.example.client;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import beans.*;
import com.googlecode.openbeans.XMLDecoder;
import utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

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
			TimeoutClient cl = new TimeoutClient(serverURL);
			FilterListForTimetable fl = new FilterListForTimetable(filters, dayNumber);
			String requestXML = Utils.encoderWrap(fl.serialize());
			String answerXML  = cl.execute(requestXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			dayTimetable = (DayTimetable) decoder.readObject();

			return dayTimetable != null;

		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "FilterDayTimetableSendTask IOException error!");
		} catch (TimeoutException e) {
			Log.d("ANL", "FilterDayTimetableSendTask timeout!");
		}

		return false;
	}

	@Override
	public void onPostExecute(Boolean result) {
		Log.d("DAN", "FilterDayTimetableSendTask.onPostExecute");
		if (!result || dayTimetable == null) {
			Toast.makeText(calendarActivity, "fail connection with server", Toast.LENGTH_SHORT).show();
			calendarActivity.finishActivity(11);
			calendarActivity.finishActivity(10);
			calendarActivity.finish();
		} else {
			calendarActivity.onFilterDayTimetableSendTask(dayTimetable, dayNumber);
		}
	}

	private ArrayList<Filter> filters;
	private int               dayNumber;
	private DayTimetable      dayTimetable;
	private URL               serverURL;
	private CalendarActivity calendarActivity;
}