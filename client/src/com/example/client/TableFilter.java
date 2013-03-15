package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;

/**
 * Class realizes a filter listView for timetable with multiple choice.
 * @author danya
 */
public class TableFilter extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablefilter);

		resultOfChoice = new ArrayList<String>();

		lvMain = (ListView) findViewById(R.id.lvMain);
		lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// Смотрим в какой фтльтр выбран, создаем адаптер, используя соответствующий массив из файла ресурсов.
		// + запоняем resourceArray всё тем же массивом
		String filterCase = this.getIntent().getStringExtra("filterNumber");
		//заодно инициализируем 0-ой элемент resultOfChoice
		resultOfChoice.add(filterCase);
		ArrayAdapter<CharSequence> adapter;
		if (filterCase.equals("countryFilter"))
		{
			adapter = ArrayAdapter.createFromResource(
					this, R.array.country_array, android.R.layout.simple_list_item_multiple_choice);
			resourceArray = getResources().getStringArray(R.array.country_array);
		} else if (filterCase.equals("sportsFilter")) {
			adapter = ArrayAdapter.createFromResource(
					this, R.array.sport_array, android.R.layout.simple_list_item_multiple_choice);
			resourceArray = getResources().getStringArray(R.array.sport_array);
		} else { // быдлохрень. Просто надо, чтобы адаптор  любом случае был инициализирован
			adapter = ArrayAdapter.createFromResource(
					this, R.array.sport_array, android.R.layout.simple_list_item_multiple_choice);
			resourceArray = getResources().getStringArray(R.array.country_array);
		}
		lvMain.setAdapter(adapter);

		Button buttonTableFilterOk = (Button) findViewById(R.id.buttonTableFilterOk);
		buttonTableFilterOk.setOnClickListener(this);
	}

	public void onClick(View view) {
		switch (view.getId()){
			case R.id.buttonTableFilterOk:
				// заполняем resultOfChoice
				SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
				for (int i = 0; i < sbArray.size(); i++) {
					int key = sbArray.keyAt(i);
					if (sbArray.get(key)) {
						resultOfChoice.add(resourceArray[key]);
					}
				}
				//передаём массив результата
				Intent intent = new Intent();
				intent.putExtra("resultOfChoice", resultOfChoice);
				setResult(RESULT_OK, intent);

				// убиваем формочку
				this.finish();
				break;
			default:
				break;
		}
	}

	private ListView lvMain; // сам listView
	private String[] resourceArray; // массив,эл-ты которого будут эл-тами lvMain. Этот массив получается из ресурсов.
	private ArrayList<String> resultOfChoice;// в 0-ом лежит имя фильтра
}