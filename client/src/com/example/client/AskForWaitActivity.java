package com.example.client;

import android.app.Activity;
import android.os.Bundle;

public class AskForWaitActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait_activity);

		// Устанавливаем текст.
		// ((TextView) findViewById(R.id.waitActivityTextView)).setText(this.getIntent().getStringExtra("textToSet"));
	}

	/**
	 * Closes this activity.
	 */
	public void finishAskForWaitActivity() {
		this.finish();
	}
}
