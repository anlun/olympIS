package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import beans.CountryApplication;
import utils.RequestResponseConst;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class ApplicationSendTask extends AsyncTask<String, Integer, Boolean> {
	public ApplicationSendTask(CountryApplication countryApplication, URL serverURL) {
		this.countryApplication = countryApplication;
		this.serverURL          = serverURL;
	}

	@Override
	protected Boolean doInBackground(String... data) {
		boolean result = false;

		try {
			String requestXML = Utils.encoderWrap(countryApplication.serialize());
			TimeoutClient cl = new TimeoutClient(serverURL);
			String answerXML = cl.execute(requestXML);

			Log.d("ANL", answerXML);

			result = getResultFromXML(answerXML);
		} catch (IOException e) {
			Log.d("ANL", "XML generation error!");
			return false;
		} catch (TimeoutException e) {
			Log.d("ANL", "ApplicationSendTask timeout!");
		}

		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			Log.d("ANL", "Application sending task was successfully done.");
		} else {
			Log.d("ANL", "Application sending task failed!");
		}

		//TODO: добавить обработку со стороны вьюшки
	}

	private static boolean getResultFromXML(String answerXML) {
		return answerXML.equals(
				RequestResponseConst.successCountryApplicationResponse()
		);
	}

	private final CountryApplication countryApplication;
	private final URL serverURL;
}
