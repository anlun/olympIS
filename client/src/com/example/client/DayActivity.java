package com.example.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Class is a view of a day is selected in a calendar. It have a dialog theme.
 * @author danya
 */
public class DayActivity extends Activity implements OnClickListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_view);

		 findViewById(R.id.dayViewCloseButton).setOnClickListener(this);

		//устанавливаем №дня
		((TextView) findViewById(R.id.dayNumberTextView)).setText("day №" + this.getIntent().getStringExtra("dayNumber"));

        // Установка расписания.
		((TextView) findViewById(R.id.dayTimetableTextView)).setText(this.getIntent().getStringExtra("dayTimetable"));
    }

	public void onClick(View view){
		switch (view.getId()){
			case R.id.dayViewCloseButton:
				this.finish();
				break;
		}
	}
}