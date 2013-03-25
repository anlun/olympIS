package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;
import beans.CountryApplication;
import beans.Athlete;
import beans.Sex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class realize completing an application GUI for authorized country.
 *  @author danya
 */
public class CountryGUI extends Activity implements OnClickListener, View.OnLongClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postrequest);

		// TODO вместо след.строчки должно быть получение уже имеющейся заявки от базы
		athleteList = new ArrayList<Athlete>();

		forceEdit = false;
		oldAthleteName = "";
		text1 = (EditText)findViewById(R.id.text1);
		text2 = (EditText)findViewById(R.id.text2);
		text3 = (EditText)findViewById(R.id.text3);
		text4 = (EditText)findViewById(R.id.text4);
		tableLayout = (TableLayout) findViewById(R.id.table1);

		addButton = (Button)findViewById(R.id.add_button);
		addButton.setOnClickListener(this);

		sp = (Spinner) findViewById(R.id.competitionSpinner);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Пункт для отправки заявки на сервер.
		menu.add(0, 1, 0, "post application");
		return super.onCreateOptionsMenu(menu);
	}

	// обновление меню
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	// обработка нажатий
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case 1:// post application
				AuthorizationData data = AuthorizationData.getInstance();
				countryApplication = new CountryApplication(data.getLogin(), data.getPassword(), athleteList);
				//TODO: дописать передачу countryApplication через Тошин класс
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Adds dynamically a row in a table.
	 * @param index Is an index in witch new Row will be added in tableLayout.
	 */
	public void addRow(String name, String sex, String weight, String height, String competition, int index) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TableRow ll = (TableRow) inflater.inflate(R.layout.tablerow, null);
		// Тут надо столько TextView обработать, сколько в таблице столбцов,
		// а так же добавить спортсмена в базу.

		List<String> l = Arrays.asList(name, sex, weight, height, competition);
		for (int i = 0; i <= 4; i++) {
			((TextView) ll.getChildAt(i)).setText(l.get(i));
		}

		// Листенер долгого нажатия, для правки иформации о спортсмене.
		ll.setOnLongClickListener(this);

		tableLayout.addView(ll, index);
	}

	// Считаю, что long click есть только у tableRow.
	public boolean onLongClick(View v) {
		TableRow lr = (TableRow) v;

		text1.setText(((TextView)lr.getChildAt(0)).getText() + "");
		text2.setText(((TextView)lr.getChildAt(1)).getText() + "");
		text3.setText(((TextView)lr.getChildAt(2)).getText() + "");
		text4.setText(((TextView)lr.getChildAt(3)).getText() + "");

		String competition = ((TextView)lr.getChildAt(4)).getText() + "";
		int itemSelected = 0;
		String[] choose = getResources().getStringArray(R.array.sport_array);
		for (itemSelected = 0; itemSelected < choose.length; itemSelected++) {
			if (choose[itemSelected].equals(competition)) {
				break;
			}
		}
		sp.setSelection(itemSelected);

		forceEdit = true;
		oldAthleteName = ((TextView)lr.getChildAt(0)).getText() + "";

		return true;
	}

	public void onClick(View v) {
		switch (v.getId()){
			case R.id.add_button:
				// TODO проверить корректность и соответствие ограничениям введенных данных

				if (!forceEdit) {
					// Имя спортсмена.
					String name = text1.getText() + "";
					int athleteIndex = getAthleteListIndex(name);
					if (athleteIndex != -1) { // Т.е. спортсмен уже есть в таблице
						Intent tableCountryFilterIntent = new Intent(this, DialogActivity.class);
						// Далее вторым параметром стоит 2. Это requestCode, он может быть любым числом.
						// Выбрана 2, т.к. 1 уже использовалось в другом классе. requestCode может совпадать
						// в разных местах программы и даже в одном классе,
						// но рекомендуется ставить разные значения, во избежания неожиданных ошибок.
						startActivityForResult(tableCountryFilterIntent, 2);
						break;
					}
					// Т.е. если не нашли атлета в списке, то вставлять его будем в начало.
					if (addAthlete(name, 0)) {
						Toast.makeText(this, "Новый спортсмен добавлен", Toast.LENGTH_SHORT).show();
					}
				} else {
					// Имя спортсмена.
					String name = oldAthleteName;
					int athleteIndex = getAthleteListIndex(name);
					// Удаляем старые данные из таблицы пользователя.
					tableLayout.removeViewAt(athleteIndex + 1);
					// Удаляем старые данные о спортсмене в athleteList.
					athleteList.remove(athleteIndex);

					if (addAthlete(name, athleteIndex)) {
						Toast.makeText(this, "Информация изменена", Toast.LENGTH_SHORT).show();
						forceEdit = false;
						oldAthleteName = "";
					}
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) return;
		if (requestCode == 2) {   // 2 соответствует параметру requestCode, передаваемому диалоговому окну при инициализации.
			if (resultCode == RESULT_OK) {
				boolean result = data.getBooleanExtra("dialogResult", false);
				if (result) {
					String name = text1.getText() + "";
					int athleteIndex = getAthleteListIndex(name);
					// Удаляем старые данные из таблицы пользователя.
					tableLayout.removeViewAt(athleteIndex + 1);
					// Удаляем старые данные о спортсмене в athleteList.
					athleteList.remove(athleteIndex);

					if (addAthlete(name, athleteIndex)) {
						Toast.makeText(this, "Информация о спортсмене " + name + " изменёна", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	private Sex toSex(String str) {
		if (str.equals("Male")) {
			return new Sex(Sex.male);
		} else if (str.equals("Female")) {
			return new Sex(Sex.female);
		} else {
			return new Sex(Sex.undefined);
		}
	}

	/**
	 * Adds athlete in a athleteList and adds an row in the user table.
	 * If athlete is successfully added, returns true, returns false otherwise.
	 * @param name Is an athlete t be added name.
	 * @param athleteIndex Is an index in witch new athlete will be added in the list and user table.
	 */
	private boolean addAthlete(String name, int athleteIndex) {
		// Получение данных из spinner-а(соревнование).
		String[] choose = getResources().getStringArray(R.array.sport_array);
		// Добавляем данные в список, который будем передавать.
		try {
			Sex sex = toSex(text2.getText() + "");
			// TODO пока что weight и height обязательны для заполнения. Если надо - можно исправить.
			int weight = Integer.parseInt((text3.getText() + ""));
			int height = Integer.parseInt((text4.getText() + ""));
			athleteList.add(athleteIndex, new Athlete(name, sex, weight,
					height, choose[sp.getSelectedItemPosition()]));
			// Добавляем информацию в таблицу пользователя.
			addRow(text1.getText() + "", sex + "", height + "",
					height + "", choose[sp.getSelectedItemPosition()], athleteIndex + 1);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Вес или рост введены некорректно.", Toast.LENGTH_SHORT).show();
			return false;
		}

		text1.setText(""); text2.setText("");
		text3.setText(""); text4.setText("");
		return true;
	}

	/**
	 * Returns index, in witch athlete is stored in the athleteList.
	 * If it is no athlete with the name given, returns -1.
	 * @param name Is the athlete name.
	 */
	private int getAthleteListIndex (String name) {
		for (int i = 0; i < athleteList.size(); i++) {
			if (athleteList.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private boolean forceEdit; // при изменении информации об спортсмене, путём долгого нажатия,
								// становится истиной. Если она true, то диалога изменения не будет.
	private String oldAthleteName; // При изменении имени, надо запомнить старое. Считаю, что имя - ключ.
	private Button addButton;
	private EditText text1;
	private EditText text2;
	private EditText text3;
	private EditText text4;
	private Spinner sp;
	private TableLayout tableLayout;

	private CountryApplication countryApplication;
	private ArrayList<Athlete> athleteList; // список атлетов, как объектов класса Athlete.
											// Этот список будет передаваться серверу.
											// Спортсмены хранятся в этом списке и отображаются
											// в таблице на экране в одном и том же порядке.
}