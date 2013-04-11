package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		Log.d("DAN","TableFilter enter");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_filter);

		resultOfChoice = new ArrayList<String>();

		lvMain = (ListView) findViewById(R.id.lvMain);
		lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// Смотрим в какой фильтр выбран, создаем адаптер, используя соответствующий массив из файла ресурсов.
		// + запоняем resourceArray всё тем же массивом
		String filterCase = this.getIntent().getStringExtra("filterNumber");
		//заодно инициализируем 0-ой элемент resultOfChoice
		resultOfChoice.add(filterCase);
		ArrayAdapter<CharSequence> adapter;
		if (filterCase.equals("countryFilter") || filterCase.equals("sportsFilter"))
		{
			resourceArray = this.getIntent().getStringArrayListExtra("resourceArray");
			adapter = new ArrayAdapter(this,
					android.R.layout.simple_list_item_multiple_choice, resourceArray);
		} else { // быдлохрень. Просто надо, чтобы адаптор  любом случае был инициализирован
			resourceArray = new ArrayList<String>();
			adapter = new ArrayAdapter(this,
					android.R.layout.simple_list_item_multiple_choice, resourceArray);
		}
		lvMain.setAdapter(adapter);

		// Получаем уже выбранные эл-ты.
		ArrayList<String> filter = this.getIntent().getStringArrayListExtra("filterIsAlreadySelectedItems");
		Log.d("DAN", "получили фильтр! " + filter.toString());
		// Если не пуст, то надо заполнить уже выбранными эл-тами.
		if (!filter.isEmpty()) {
			int j = 0;
			Log.d("DAN", "зашли в if!");
			for (int i = 0; i < resourceArray.size(); i++) {
				if (j >= filter.size()) {
					break;
				}
				if (filter.get(j).equals(resourceArray.get(i))) {
					lvMain.setItemChecked(i, true);
					j++;
				}
			}
		}
		Log.d("DAN", "вышли!");

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
						resultOfChoice.add(resourceArray.get(key));
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
	private ArrayList<String> resourceArray; // массив,эл-ты которого будут эл-тами lvMain. Этот массив получается из ресурсов.
	private ArrayList<String> resultOfChoice;// в 0-ом лежит имя фильтра
}