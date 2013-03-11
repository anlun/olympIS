package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import java.util.ArrayList;

/**
 * Class - activity realized the full timetable GUI
 */
public class CalendarActivity extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);

		//устанавливаем onClickListener для фильтров
		(findViewById(R.id.countryFilter)).setOnClickListener(this);
		(findViewById(R.id.sportsFilter)).setOnClickListener(this);

		//устанавливаем onClickListener для всех дней
		LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);
		for(Integer j = 1; j < 4 ; j++)  {
			LinearLayout ll1 = (LinearLayout) ll.getChildAt(j);
			for(Integer i = 0; i < 7 ; i++ ){
				TextView tv = (TextView) ll1.getChildAt(i);
				tv.setOnClickListener(this);
			}
		}
	}

	/**
	 * To do on click
	 * @param view If it is a click on a day open an a day timetable in a day activity.(look at default)
	 */
	@Override
	public void onClick(View view){
		switch (view.getId()){
			case R.id.countryFilter:
				Intent tableCountryFilterIntent = new Intent(this, TableFilter.class);
				tableCountryFilterIntent.putExtra("filterNumber", "countryFilter");
				startActivityForResult(tableCountryFilterIntent, 1);
				break;
			case R.id.sportsFilter:
				Intent tableSportsFilterIntent = new Intent(this, TableFilter.class);
				tableSportsFilterIntent.putExtra("filterNumber", "sportsFilter");
				startActivityForResult(tableSportsFilterIntent, 1);
				break;
			default:
				Intent dayActivityIntent = new Intent(this, DayActivity.class);
				dayActivityIntent.putExtra("dayNumber", ((TextView) view).getHint());
				startActivity(dayActivityIntent);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) return;
		if (requestCode == 1) {   //т.е. фильтер
			if (resultCode == RESULT_OK) {
				ArrayList<String> result = data.getStringArrayListExtra("resultOfChoice");
				//сей result есть результат выбора в ListView пользователем.
				//первый элемент массива - название фильтра
				Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
			}
			else if (resultCode == RESULT_CANCELED) {
			}
		}
	}
}