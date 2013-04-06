package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import beans.DayTimetable;
import beans.Filter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class - activity realizes the full timetable GUI.
 */
public class CalendarActivity extends Activity implements OnClickListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);

		// TODO получить фильтры от базы!

		filterList = new ArrayList<Filter>();
		authorizationData = AuthorizationData.getInstance();

		//устанавливаем onClickListener для фильтров
		(findViewById(R.id.countryFilter)).setOnClickListener(this);
		(findViewById(R.id.sportsFilter)).setOnClickListener(this);

		//устанавливаем onClickListener для всех дней
		LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);
		for(Integer j = 1; j < numberOfWeeks ; j++)  {
			LinearLayout ll1 = (LinearLayout) ll.getChildAt(j);
			for(Integer i = 0; i < 7 ; i++ ){
				TextView tv = (TextView) ll1.getChildAt(i);
				tv.setOnClickListener(this);
			}
		}
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
			default: // т.е. клик по дню.
				// TODO в след строчке нужно пихать реальное расписание, полученное от сервера в виде строки
				// вешаем гуи.
				startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);

				// передаю собственно filterList
				try {
					(new FilterDayTimetableSendTask(filterList,
							Integer.parseInt(((TextView) view).getHint().toString()),
							new URL("http://178.130.32.141:8888"), this)).execute();
				} catch (MalformedURLException e) {
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) return;
		if (requestCode == 1) {   //т.е. фильтер
			if (resultCode == RESULT_OK) {
				try {
					// вешаю гуи
					startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);

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

					// передаю собственно filterList
					(new FilterDayListSendTask(filterList, new URL("http://178.130.32.141:8888"), this)).execute();

					Toast.makeText(this, filterName + result.toString(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
	                Toast.makeText(this, "Неизвестная ошибка при работе с фильтрами.", Toast.LENGTH_LONG).show();
				}
			}
			else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

	// получить ответ в виде DayList
	public void onFilterDayListSendTask(ArrayList<Integer> dayList) {
		Log.d("DAN","onFilterDayListSendTask enter");
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i <= 21; i++) {
			ar.add(i);
		}
		setColor(ar, Color.WHITE);
		Log.d("DAN","onFilterDayListSendTask exit");
		setColor(dayList, Color.GREEN);

		finishActivity(10);
	}

	// получить ответ в виде DayTimetable
	public void onFilterDayTimetableSendTask(DayTimetable dayTimetable, int dayNumber) {
		// TODO отобразить dayTimetable
		Log.d("DAN","получили dayTimetable от сервера.");
		Intent dayActivityIntent = new Intent(this, DayActivity.class);
		dayActivityIntent.putExtra("dayNumber", dayNumber + "");
		Log.d("DAN","1");
		String str = "";
		for (int i = 0; i < dayTimetable.getDayTimetable().size(); i++) {
			Log.d("DAN","index " + i);
			str += dayTimetable.getDayTimetable().get(i).getSportElement() + "\n";
		}
		Log.d("DAN","2");
		dayActivityIntent.putExtra("dayTimetable", str);
		Log.d("DAN","3");
		startActivity(dayActivityIntent);

		Log.d("DAN","убили активити временное");
		finishActivity(10);
		Log.d("DAN","вышли из onFilterDayListSendTask.");
	}

	/**
	 * Set background color to the days from dayList, if day >= firstDay and day <= lastDay.
	 * @param dayList Is a list of days to set background color.
	 * @param color Is a color set to.(Example: Color.GREEN) If you want to set default color
	 *              set Color.WHITE.
	 */
	private void setColor(ArrayList<Integer> dayList, int color) {
		for(Integer i: dayList) {
			if (i >= firstDay && i <= lastDay) {
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

	private final static int firstDay = 1; // First day of competitions.
	private final static int lastDay = 21; // Last day of competitions.
	private final static int numberOfWeeks = 4; // Weeks count.
	private ArrayList<Filter> filterList;
	private AuthorizationData authorizationData;
}