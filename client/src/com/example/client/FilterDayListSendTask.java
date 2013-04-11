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
import java.util.concurrent.TimeoutException;

public class FilterDayListSendTask extends AsyncTask<String, Integer, Boolean> {
	public FilterDayListSendTask(ArrayList<Filter> filters, URL serverURL, CalendarActivity calendarActivity) {
		this.filters    = filters;
		this.serverURL = serverURL;
		this.calendarActivity = calendarActivity;
	}

	@Override
	public Boolean doInBackground(String... data) {
		try {
			TimeoutClient cl = new TimeoutClient(serverURL);
			FilterListForDayList fl = new FilterListForDayList(filters);
			String requestXML = Utils.encoderWrap(fl.serialize());
			String answerXML  = cl.execute(requestXML);

			XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(answerXML.getBytes("UTF-8")));
			dayList = (DayList) decoder.readObject();

			return dayList != null;

		} catch (UnsupportedEncodingException e) {
			Log.d("ANL", "Unsupported encoding error!");
		} catch (IOException e) {
			Log.d("ANL", "FilterSendTask IOException error!");
		} catch (TimeoutException e) {
			Log.d("ANL", "FilterDayListSendTask timeout!");
		}

		return false;
	}

	@Override
	public void onPostExecute(Boolean result) {
		//TODO: Danya - dayList may be null!!!
		calendarActivity.onFilterDayListSendTask(dayList.getListOfDays());
	}

	private ArrayList<Filter> filters;
	private DayList           dayList;
	private URL               serverURL;
	private CalendarActivity calendarActivity;
}
