package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import beans.CountryApplication;
import utils.RequestResponseConst;
import utils.Utils;

import java.io.IOException;
import java.net.URL;

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
			Client cl = new Client(serverURL);
			String answerXML = cl.execute(requestXML);

			Log.d("ANL", answerXML);

			result = getResultFromXML(answerXML);
		} catch (IOException e) {
			Log.d("ANL", "XML generation error!");
			return false;
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
		Toast.makeText(countryGUIObject, "application has sent to server",Toast.LENGTH_LONG).show();
		countryGUIObject.doFinish();
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
