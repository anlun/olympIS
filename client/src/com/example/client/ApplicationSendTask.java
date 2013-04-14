package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import beans.CountryApplication;
import utils.RequestResponseConst;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class ApplicationSendTask extends AsyncTask<String, Integer, Boolean> {
	public ApplicationSendTask(CountryApplication countryApplication, URL serverURL, CountryGUI countryGUIObject) {
		this.countryApplication = countryApplication;
		this.serverURL          = serverURL;
		this.countryGUIObject = countryGUIObject;
	}

	@Override
	protected Boolean doInBackground(String... data) {
		boolean result = false;

		try {
			String requestXML = Utils.encoderWrap(countryApplication.serialize());
			//15 - seconds to timeout. Extended from default according to github.com/anlun/olympIS/issues/73
			TimeoutClient cl = new TimeoutClient(15, serverURL);
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
		countryGUIObject.onApplicationSendTask(result);
	}

	private static boolean getResultFromXML(String answerXML) {
		return answerXML.equals(
				RequestResponseConst.successCountryApplicationResponse()
		);
	}

	private final CountryApplication countryApplication;
	private final URL serverURL;
	private CountryGUI countryGUIObject;
}
