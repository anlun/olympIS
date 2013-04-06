package com.example.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class AskForWaitActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wait_activity);
		Log.d("DAN", "ask wait started. OK.");
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d("DAN", "переназначили кнопку, чтобы не закрывалось Activity");
		return true;
	}

}
