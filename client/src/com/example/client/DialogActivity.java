package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.TextView;

public class DialogActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);

		((TextView)findViewById(R.id.dialog_text)).setText("This athlete is in the list. You are really want to edit it?");

		findViewById(R.id.yes_button).setOnClickListener(this);
		findViewById(R.id.no_button).setOnClickListener(this);
	}

	public void onClick(View view){
		switch (view.getId()){
			case R.id.yes_button:
				Intent intent = new Intent();
				intent.putExtra("dialogResult", true);
				setResult(RESULT_OK, intent);
				this.finish();
				break;
			case R.id.no_button:
				Intent intent1 = new Intent();
				intent1.putExtra("dialogResult", false);
				setResult(RESULT_OK, intent1);
				this.finish();
				break;
		}
	}
}
