package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import beans.DayTimetable;
import beans.Filter;
import utils.Utils;

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

		filterList = new ArrayList<Filter>();
		serverFilterList = new ArrayList<Filter>();

		// получаем фильтры от базы!
		try {
			(new FilterGetTask(new URL(Utils.serverAddress), this)).execute();
			startActivityForResult(new Intent(this, AskForWaitActivity.class), 11);
		} catch (Exception e) {
			Log.d("DAN", e.toString());
		}

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

	public void onFilterGetTask(ArrayList<Filter> serverFilterList) {
		if (serverFilterList == null) {
			Toast.makeText(this, "fail connection", Toast.LENGTH_SHORT).show();
			this.finish();
			return;
		}
		this.serverFilterList = serverFilterList;
		finishActivity(11);
	}

	/**
	 * To do on click. If it is a click on a day open an a day timetable in a day activity.(look at default)
	 * @param view Is a view licked on.s
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.countryFilter:
			case R.id.sportsFilter:
				Log.d("DAN","enter in case");
				String filterName = "";
				if (view.getId() == R.id.sportsFilter) {
					filterName =  "sportsFilter";
				} else {
					filterName =  "countryFilter";
				}

				Intent tableCountryFilterIntent = new Intent(this, TableFilter.class);
				tableCountryFilterIntent.putExtra("filterNumber", filterName);

				// передаём список
				int filterIndex = getFilterIndexFromFilterList(filterName, serverFilterList);
				if (filterIndex == -1) {
					Log.d("DAN","break.");
					break;
				} else {
					tableCountryFilterIntent.putStringArrayListExtra("resourceArray",
							serverFilterList.get(filterIndex).getFilter());
				}

				// Передаём уже выбранные эл-ты.
				int index = getFilterIndexFromFilterList(filterName, filterList);
				if (index != -1) {
					tableCountryFilterIntent.putExtra("filterIsAlreadySelectedItems", filterList.get(index).getFilter());
				} else {
					tableCountryFilterIntent.putExtra("filterIsAlreadySelectedItems", new ArrayList<String>());
				}

				startActivityForResult(tableCountryFilterIntent, 1);
				break;
			default: // т.е. клик по дню.
				// вешаем гуи.
				startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);

				// передаю собственно filterList
				try {
					(new FilterDayTimetableSendTask(filterList,
							Integer.parseInt(((TextView) view).getHint().toString()),
							new URL(Utils.serverAddress), this)).execute();
				} catch (MalformedURLException e) {
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("DAN", "onActivityResult starts");
		if (data == null || requestCode == 11) return;
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
						int index = getFilterIndexFromFilterList(filterName, filterList);
						if (index != -1) {
							filterList.remove(index);
						}
						return;
					}
					addFilter(filterName, result);

					// передаю собственно filterList
					(new FilterDayListSendTask(filterList, new URL(Utils.serverAddress), this)).execute();

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
		Log.d("DAN","onFilterDayListSendTask " + dayList.size() + " " + dayList.toString());
		try {
			ArrayList<Integer> ar = new ArrayList<Integer>();
			for (int i = 1; i <= 21; i++) {
				ar.add(i);
			}
			setColor(ar, Color.WHITE);
			if (dayList != null) {
				setColor(dayList, Color.GREEN);
			}
		} catch (Exception e) {
			Log.d("DAN", "поймали exception в onFilterDayListSendTask.(CalendarActivity). Ответ от сервера некорректен.");
			// говорим юзеру, что мол якобы нет соединения с сервером.
			Toast.makeText(this, "fail! No connection with server.", Toast.LENGTH_SHORT).show();
			// закрываем AskForWaitActivity и это активити тоже.
			finish();
		}
		finishActivity(10);
	}

	// получить ответ в виде DayTimetable
	public void onFilterDayTimetableSendTask(DayTimetable dayTimetable, int dayNumber) {
		Log.d("DAN","onFilterDayTimetableSendTask");
		if (dayTimetable == null) {
			Log.d("DAN","dayTimetable == null.");
			return;
		}
		// TODO отобразить dayTimetable
		try {
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
		} catch (Exception e) {
			Log.d("DAN", "поймали exception в onFilterDayTimetableSendTask.(CalendarActivity). Ответ от сервера некорректен.");
			// говорим юзеру, что мол якобы нет соединения с сервером.
			Toast.makeText(this, "fail! No connection with server.", Toast.LENGTH_SHORT).show();
			// закрываем AskForWaitActivity.
			finishActivity(10);
		}
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
	private int getFilterIndexFromFilterList(String filterName, ArrayList<Filter> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFilterName().equals(filterName)) {
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
		int index = getFilterIndexFromFilterList(filterName, filterList);
		if (index != -1) {
			filterList.remove(index);
		}
		filterList.add(new Filter(filterName, resultOfChoice));
	}

	private final static int firstDay = 1; // First day of competitions.
	private final static int lastDay = 21; // Last day of competitions.
	private final static int numberOfWeeks = 4; // Weeks count.
	private ArrayList<Filter> filterList;
	private ArrayList<Filter> serverFilterList;
}