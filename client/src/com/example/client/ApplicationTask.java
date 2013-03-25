package com.example.client;

import android.os.AsyncTask;
import android.util.Log;
import beans.CountryApplication;
import utils.Utils;

import java.io.IOException;
import java.net.URL;

public class ApplicationTask extends AsyncTask<String, Integer, Boolean> {
	public ApplicationTask(CountryApplication countryApplication, URL serverURL) {
		this.countryApplication = countryApplication;
		this.serverURL          = serverURL;
	}

	@Override
	protected Boolean doInBackground(String... data) {
		boolean result = false;
		try {
			String requestXML = Utils.beanToString(countryApplication);
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
	}

	//TODO: сделать нормальный парсинг XML ответа
	private static boolean getResultFromXML(String answerXML) {
		return false;
	}

	private CountryApplication countryApplication;
	private URL serverURL;
}
