package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AskForWaitActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait_activity);
		Log.d("DAN", "ask wait started");
		// Устанавливаем текст.
		// ((TextView) findViewById(R.id.waitActivityTextView)).setText(this.getIntent().getStringExtra("textToSet"));
	}

	/**
	 * Closes this activity.
	 */
	public void finishAskForWaitActivity() {
		Intent intent1 = new Intent(this, CountryGUI.class);
		startActivity(intent1);
		this.finish();
	}
}
