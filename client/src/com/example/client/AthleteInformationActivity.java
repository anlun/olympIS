package com.example.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AthleteInformationActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("DAN","activity started");
		super.onCreate(savedInstanceState);
		Log.d("DAN","setContent");
		setContentView(R.layout.athlete_information);
		Log.d("DAN","linearLayout init");
		linearLayout = (LinearLayout) findViewById(R.id.athleteInformationMainLinearLayout);
		Log.d("DAN","setText");
		((TextView) linearLayout.getChildAt(0)).setText(this.getIntent().getStringExtra("athleteInformation"));
	}

	private LinearLayout linearLayout;
}
