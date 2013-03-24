package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import beans.Filter;
import beans.DayList;

import java.util.ArrayList;

/**
 * Class - activity realizes the full timetable GUI.
 */
public class CalendarActivity extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);

		filterList = new ArrayList<Filter>();

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

		// TODO убрать эти 8 строчек. Они сейчас для наглядности работы метода setColor.
		ArrayList<Integer> ar = new ArrayList<Integer>();
		ArrayList<Integer> ar1 = new ArrayList<Integer>();
		for (int i = 0; i <= 30; i++) {
			ar.add(i);
			if (i % 2 == 0) ar1.add(i);
		}
		setColor(ar, Color.GREEN);
		setColor(ar1, Color.WHITE);
	}

	/**
	 * To do on click. If it is a click on a day open an a day timetable in a day activity.(look at default)
	 * @param view Is a view licked on.s
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.countryFilter:
				Intent tableCountryFilterIntent = new Intent(this, TableFilter.class);
				tableCountryFilterIntent.putExtra("filterNumber", "countryFilter");

				// Передаём уже выбранные эл-ты.
				int index = getFilterIndexFromFilterList("countryFilter");
				if (index != -1) {
					tableCountryFilterIntent.putExtra("filterIsAlreadySelectedItems", filterList.get(index).getFilter());
				} else {
					tableCountryFilterIntent.putExtra("filterIsAlreadySelectedItems", new ArrayList<String>());
				}

				startActivityForResult(tableCountryFilterIntent, 1);
				break;
			case R.id.sportsFilter:
				Intent tableSportsFilterIntent = new Intent(this, TableFilter.class);

				// Передаём уже выбранные эл-ты.
				int index1 = getFilterIndexFromFilterList("sportsFilter");
				if (index1 != -1) {
					tableSportsFilterIntent.putExtra("filterIsAlreadySelectedItems", filterList.get(index1).getFilter());
				} else {
					tableSportsFilterIntent.putExtra("filterIsAlreadySelectedItems", new ArrayList<String>());
				}

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
				try {
					//сей result есть результат выбора в ListView пользователем.
					//первый элемент массива - название фильтра
					ArrayList<String> result = data.getStringArrayListExtra("resultOfChoice");
					String filterName = result.get(0);
					result.remove(0);
					if (result.isEmpty()) { // Т.е. в фильтре ничего не выбрано.
						int index = getFilterIndexFromFilterList(filterName);
						if (index != -1) {
							filterList.remove(index);
						}
						return;
					}
					addFilter(filterName, result);

					// TODO передать собственно filterList и получить ответ в виде DayList
					// setSelectedDaysGreen(dayList);

					Toast.makeText(this, filterName + result.toString(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
	                Toast.makeText(this, "Неизвестная ошибка при работе с фильтрами.", Toast.LENGTH_LONG).show();
				}
			}
			else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

	/**
	 * Set background color to the days from dayList, if day >= 1 and day <=21.
	 * @param dayList Is a list of days to set background color.
	 * @param color Is a color set to.(Example: Color.GREEN) If you want to set default color
	 *              set Color.WHITE.
	 */
	private void setColor(ArrayList<Integer> dayList, int color) {
		for(Integer i: dayList) {
			if (i >=1 && i <= 21) {
				// За грядущеё 5 строчек стыдно, но ниче умнее в голову не пришло.
				// j - номер строки. k - номер стобца.(Это про то место, где расположен днь на экране)
				int j = i / 7;
				int k = i - j * 7 - 1;
				j++;
				if (k == -1) {
					k = 6; j--;
				}
				LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);
				LinearLayout ll1 = (LinearLayout) ll.getChildAt(j);
				TextView tv = (TextView) ll1.getChildAt(k);
				tv.setBackgroundColor(color);
			} else {
				Log.d("DAN", "Incorrect day in dayList: " + i + "Look at CalendarActivity setSelectedDaysGreen." +
						" Or incorrect color.");
			}
		}
	}

	/**
	 * Returns index in witch Filter with filterName is stored in filterList.
	 * If filterList doesn't contains Filter with Filter.filterName == filterName,
	 * returns -1.
	 */
	private int getFilterIndexFromFilterList(String filterName) {
		for (int i = 0; i < filterList.size(); i++) {
			if (filterList.get(i).getFilterName().equals(filterName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Adds the new Filter with Filter.filterName == filterName and
	 * Filter.filter == resultOfChoice. If filterList contains Filter with
	 * Filter.filterName == filterName, this Filter will be removed and
	 * new Filter will be added.
	 */
	private void addFilter(String filterName, ArrayList<String> resultOfChoice) {
		int index = getFilterIndexFromFilterList(filterName);
		if (index != -1) {
			filterList.remove(index);
		}
		filterList.add(new Filter(filterName, resultOfChoice));
	}

	private ArrayList<Filter> filterList;
}