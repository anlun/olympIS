package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;
import beans.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class realize completing an application GUI for authorized country.
 *  @author danya
 */
public class CountryGUI extends Activity implements OnClickListener, View.OnLongClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_application);

		// competitionNamesList = getResources().getStringArray(R.array.sport_array);
		sexArray = getResources().getStringArray(R.array.sex_array);
		/*
		athleteNumberList = new int[competitionNamesList.length];
		// Задаём число спортсменов для каждого соревнования.
		for (int i = 0 ; i < athleteNumberList.length; i++) {
			athleteNumberList[i] = 20;
		}
		*/
		// competitionList = new CompetitionList(competitionNamesList, athleteNumberList);

		forceEdit = false;
		oldAthleteName = "";
		nameTextEdit = (EditText)findViewById(R.id.nameTextEdit);
		sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
		weightTextEdit = (EditText)findViewById(R.id.weightTextEdit);
		heightTextEdit = (EditText)findViewById(R.id.heightTextEdit);
		linearLayout = (LinearLayout) findViewById(R.id.linLayMain);

		// LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayoutArrayList = new ArrayList<LinearLayout>();
		/*
		for (int i = 0; i < competitionNamesList.length; i++) {
			LinearLayout lv = (LinearLayout) inflater.inflate(R.layout.linear_layout_pattern, null);
			linearLayoutArrayList.add(lv);
		}
		linearLayout.addView(linearLayoutArrayList.get(0));
		*/
		athleteCompetitionNumber = (TextView) findViewById(R.id.athleteCompetitionNumber);

		findViewById(R.id.add_button).setOnClickListener(this);

		sp = (Spinner) findViewById(R.id.competitionSpinner);
		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			// Вызывается при смене Item в спиннере sp.
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				forceEdit = false;
				// Удаляем старый список спортсменов из видимости.
				linearLayout.removeViewAt(0);
				// Делаем видимым список спортсменов, соответствующий соревнованию.
				linearLayout.addView(linearLayoutArrayList.get(position));
				String competition = competitionNamesList[sp.getSelectedItemPosition()];
				// Выставляем число атлетов в заявке и макс.число атлетов по данному соревнованию.
				int athleteNumber = competitionList.getAthleteNumber(competition);
				int athleteMaxNumber = competitionList.getMaxAthleteNumber(competition);
				athleteCompetitionNumber.setText("Осталось: " + (athleteMaxNumber - athleteNumber) +
						"/" + athleteMaxNumber);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// получение уже имеющейся заявки от базы + число спортсменов
		authorizationData = AuthorizationData.getInstance();
		// countryApplication = new CountryApplication(data.getLogin(), data.getPassword(), new CompetitionList());
		ExistApplicationGetTask task = new ExistApplicationGetTask(
				authorizationData.getLogin(), authorizationData.getPassword(), authorizationData.getServerURL(), this);
		task.execute();

		// Вешаем гуи пока не дождемся ответа от сервера, запуская AskForWaitActivity.
		startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);
	}

	public void getCountryApplicationFromServer(CountryApplication result) {
		this.competitionList = result.getCompetitionList();
		ArrayList<ClientCompetition>  compList = competitionList.getCompetitionList();

		if (compList.size() != 0) {
			Log.d("DAN", competitionList.toString() + "\n" + compList.size());
			// заполнить layout-ы спортсменами
			athleteNumberList = new int[compList.size()];
			competitionNamesList = new String[compList.size()];
			int i = 0;
			for (ClientCompetition competition: compList) {
				athleteNumberList[i] = competition.getMaxAthleteNumber(); // Список, содржащий количество спортсменов на каждое соревнование, которое страна может подать.
				competitionNamesList[i] = competition.getCompetition(); // Список названий соревнований.
				i++;
			}

			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int j = 0; j < competitionNamesList.length; j++) {
				LinearLayout lv = (LinearLayout) inflater.inflate(R.layout.linear_layout_pattern, null);
				linearLayoutArrayList.add(lv);
			}
			linearLayout.addView(linearLayoutArrayList.get(0));

			// Убиваем AskForWaitActivity. 10 - requestCode этого активити.
			finishActivity(10);

			// Устанавливаем массив ресурсов для спиннера.
			sp.setAdapter(new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, competitionNamesList));

			// Забиваем вьюшки спортсменами из базы.
			for (ClientCompetition competition : compList) {
				int athleteIndex = 0;
				for (Athlete athlete : competition.getAthleteCompetitionList()) {
					/* // ВРОДЕ! не нужно добавление, т.к. атлет уже должен буть там.
					competitionList.addAthlete(athleteIndex, competition.getCompetition(),
							new Athlete(athlete.getName(), athlete.getSex(),
									athlete.getWeight(), athlete.getWeight(),
									competition.getCompetition()));
                     */

					// Добавляем информацию в таблицу пользователя.

					TextView tv = (TextView) inflater.inflate(R.layout.text_view_pattern, null);
					tv.setText(athlete.getName());
					// Листенер долгого нажатия, для правки иформации о спортсмене.
					tv.setOnLongClickListener(this);
					tv.setOnClickListener(this);
					tv.setGravity(Gravity.CENTER);
					int randomColor = Color.rgb(random.nextInt() % 255, random.nextInt() % 255, random.nextInt() % 255);
					tv.setBackgroundColor(randomColor);
					tv.setTextColor(Color.rgb(Color.red(randomColor) / 2, 255 - Color.green(randomColor), 255 - Color.blue(randomColor)));

					int linearLayoutIndex = 0;
					for (linearLayoutIndex = 0; linearLayoutIndex < competitionNamesList.length; linearLayoutIndex++) {
						if (competitionNamesList[linearLayoutIndex].equals(competition.getCompetition())) {
							break;
						}
					}
					LinearLayout lv = linearLayoutArrayList.get(linearLayoutIndex);
					lv.addView(tv,athleteIndex);

					athleteIndex++;
				}
			}

		} else {
			Log.d("DAN", "убиваем CountryGUIActivity т.к. заявка, пришедшая с базы, пуста");
			finish();
		}
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
				countryApplication = new CountryApplication(data.getLogin(), data.getPassword(), competitionList);
				(new ApplicationSendTask(countryApplication, data.getServerURL(), this)).execute();

				// Вешаем гуи пока не дождемся ответа от сервера, запуская AskForWaitActivity.
				startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	* Adds dynamically a athlete in list and visible layout.
	* @param index Is an index in witch new Row will be added in layout.
	*/
	public void addRow(String name, int index) {
		Log.d("DAN","in addRow");
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("DAN","getting TextView");
		TextView tv = (TextView) inflater.inflate(R.layout.text_view_pattern, null);
		Log.d("DAN","getted TextView. set name");
		tv.setText(name);
		Log.d("DAN","name setted");

		// Листенер долгого нажатия, для правки иформации о спортсмене.
		tv.setOnLongClickListener(this);
		tv.setOnClickListener(this);
		tv.setGravity(Gravity.CENTER);
		int randomColor = Color.rgb(random.nextInt() % 255, random.nextInt() % 255, random.nextInt() % 255);
		tv.setBackgroundColor(randomColor);
		tv.setTextColor(Color.rgb(Color.red(randomColor) / 2, 255 - Color.green(randomColor), 255 - Color.blue(randomColor)));
		Log.d("DAN","OnLongClick setted");

		LinearLayout lv = (LinearLayout) linearLayout.getChildAt(0);
		Log.d("DAN","LinearLayout lv = (LinearLayout) linearLayout.getChildAt(0);");
		lv.addView(tv,index);
		Log.d("DAN","((LinearLayout) linearLayout.getChildAt(0)).addView(tv, index);");
	}

	// Считаю, что long click есть только у TextView каждого атлета.
	public boolean onLongClick(View v) {
		TextView tv = (TextView) v;
		String name = tv. getText() + "";
		String competition = competitionNamesList[sp.getSelectedItemPosition()];

		Log.d("DAN","get athlete");
		Athlete athlete = competitionList.getAthlete(name, competition);
		Log.d("DAN","get name");
		nameTextEdit.setText(athlete.getName());
		Log.d("DAN", "get sex");
		sexSpinner.setSelection(doIndexFomSex(competitionList.getAthlete(name, competition).getSex()));
		Log.d("DAN", "get weight");
		weightTextEdit.setText(athlete.getWeight() + "");
		Log.d("DAN", "get height");
		heightTextEdit.setText(athlete.getHeight() + "");

		int itemSelected;
		for (itemSelected = 0; itemSelected < competitionNamesList.length; itemSelected++) {
			if (competitionNamesList[itemSelected].equals(competition)) {
				break;
			}
		}
		sp.setSelection(itemSelected);

		forceEdit = true;
		oldAthleteName = tv.getText() + "";

		return true;
	}

	public void onClick(View v) {
		switch (v.getId()){
			case R.id.add_button:
				// TODO проверить корректность и соответствие ограничениям введенных данных

				if (!forceEdit) {
					String competition = competitionNamesList[sp.getSelectedItemPosition()];
					if (competitionList.getAthleteNumber(competition) >= competitionList.getMaxAthleteNumber(competition)) {
						Toast.makeText(this, "Вы исчерпали количество заявок.", Toast.LENGTH_LONG).show();
						return;
					}

					// Имя спортсмена.
					String name = nameTextEdit.getText() + "";
					int athleteIndex = this.competitionList.getAthleteListIndex(name, competitionNamesList[sp.getSelectedItemPosition()]);
					if (athleteIndex != -1) { // Т.е. спортсмен уже есть в таблице
						Intent dialogActivity = new Intent(this, DialogActivity.class);
						// Далее вторым параметром стоит 2. Это requestCode, он может быть любым числом.
						// Выбрана 2, т.к. 1 уже использовалось в другом классе. requestCode может совпадать
						// в разных местах программы и даже в одном классе,
						// но рекомендуется ставить разные значения, во избежания неожиданных ошибок.
						startActivityForResult(dialogActivity, 2);
						break;
					}
					// Т.е. если не нашли атлета в списке, то вставлять его будем в начало.
					if (addAthlete(0)) {
						Toast.makeText(this, "Новый спортсмен добавлен", Toast.LENGTH_SHORT).show();
						int athleteNumber = competitionList.getAthleteNumber(competition);
						int athleteMaxNumber = competitionList.getMaxAthleteNumber(competition);
						athleteCompetitionNumber.setText("Осталось: " + (athleteMaxNumber - athleteNumber) +
								"/" + athleteMaxNumber);
					}
				} else {
					// Имя спортсмена.
					String name = oldAthleteName;
					// Соревнование.
					String competition = competitionNamesList[sp.getSelectedItemPosition()];
					int athleteIndex = this.competitionList.getAthleteListIndex(name, competition);
					if (addAthlete(athleteIndex)) {
						Toast.makeText(this, "Информация изменена", Toast.LENGTH_SHORT).show();
						forceEdit = false;
						oldAthleteName = "";
					}
				}
				break;
			default: // Это значит, что это был клик по спортсмену.
				TextView tv = (TextView) v;
				String name = tv. getText() + "";
				String competition = competitionNamesList[sp.getSelectedItemPosition()];
				Athlete athlete = competitionList.getAthlete(name, competition);

				Log.d("DAN","get athleteInformation");
				String athleteInformation = "Name: " + athlete.getName() + "\n\n" +
						"Sex: " + sexToString(athlete.getSex()) + "\n\n" +
						"Weight: " + athlete.getWeight() + "\n\n" +
						"Height: " + athlete.getHeight() + "\n\n" +
						"Competition: " + athlete.getCompetition() + "";
				Log.d("DAN","new intent");
				Intent dayActivityIntent = new Intent(this, AthleteInformationActivity.class);
				dayActivityIntent.putExtra("athleteInformation", athleteInformation);
				Log.d("DAN","startActivity");
				startActivity(dayActivityIntent);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) return;
		if (requestCode == 10) { // т.е. вернулись из AskForWaitActivity
			Log.d("DAN", "перезапуск AskForWaitActivity");
			startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);
		} else if (requestCode == 2) {   // 2 соответствует параметру requestCode, передаваемому диалоговому окну при инициализации.
			if (resultCode == RESULT_OK) {
				boolean result = data.getBooleanExtra("dialogResult", false);
				if (result) {
					String name = nameTextEdit.getText() + "";
					String competition = competitionNamesList[sp.getSelectedItemPosition()];
					int athleteIndex = this.competitionList.getAthleteListIndex(name, competitionNamesList[sp.getSelectedItemPosition()]);

					// Удаляем старые данные из таблицы пользователя.
					Log.d("DAN", "delete view index " + athleteIndex);
					((LinearLayout) linearLayout.getChildAt(0)).removeViewAt(athleteIndex);

					// Удаляем старые данные о спортсмене
					competitionList.deleteAthlete(name, competition);

					if (addAthlete(athleteIndex)) {
						Toast.makeText(this, "Информация о спортсмене " + name + " изменёна", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	private Sex toSex(String str) {
		if (str.equals("Male") || str.equals("male") || str.equals("M") || str.equals("m")) {
			return new Sex(Sex.male);
		} else if (str.equals("Female") || str.equals("female") || str.equals("F") || str.equals("f")) {
			return new Sex(Sex.female);
		} else {
			return new Sex(Sex.undefined);
		}
	}

	/**
	 * Adds athlete in a athleteList and adds an row in the user list.
	 * If athlete is successfully added, returns true, returns false otherwise.
	 * @param athleteIndex Is an index in witch new athlete will be added in the list and user list.
	 */
	private boolean addAthlete(int athleteIndex) {
		// Добавляем данные в список, который будем передавать.
		try {
			String name = nameTextEdit.getText() + "";
			Sex sex = toSex(sexArray[sexSpinner.getSelectedItemPosition()]);
			// TODO пока что weight и height обязательны для заполнения. Если надо - можно исправить.
			int weight = Integer.parseInt((weightTextEdit.getText() + ""));
			int height = Integer.parseInt((heightTextEdit.getText() + ""));
			String competition = competitionNamesList[sp.getSelectedItemPosition()];

			if(forceEdit){
				// Удаляем старые данные из таблицы пользователя.
				Log.d("DAN", "delete view index " + athleteIndex);
				((LinearLayout) linearLayout.getChildAt(0)).removeViewAt(athleteIndex);
				// Удаляем старые данные о спортсмене
				Log.d("DAN", "delete list " +  name + " " + competition);
				competitionList.deleteAthlete(oldAthleteName, competition);
				Log.d("DAN", "deleted");
				forceEdit = false;
			}

			Log.d("DAN", "index " + athleteIndex);
			competitionList.addAthlete(athleteIndex, competition, new Athlete(name, sex, weight, height, competition));

			// Добавляем информацию в таблицу пользователя.
			addRow(name + "", athleteIndex);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Вес или рост введены некорректно.", Toast.LENGTH_SHORT).show();
			return false;
		}

		nameTextEdit.setText("");
		sexSpinner.setSelection(0);
		weightTextEdit.setText(""); heightTextEdit.setText("");
		return true;
	}

	private String sexToString(Sex sex) {
		switch (sex.getSex()) {
			case Sex.undefined:
				return "undefined";
			case Sex.male:
				return "male";
			case Sex.female:
				return "female";
			default:
				return "undefined";
		}
	}

	/**
	 * It is doing the index for sexArray from the Sex object.
	 * @param sex Is the Sex object.
	 * @return index for sexArray accordingly for the sexSpinner with is match the order of sex sequence in sexSpinner.
	 */
	private int doIndexFomSex(Sex sex) {
		switch (sex.getSex()) {
			case Sex.male:
				return 1;
			case Sex.female:
				return 2;
			default:
				return 0;
		}
	}

	public void doFinish() {
		finishActivity(10);
		this.finish();
	}

	private boolean forceEdit; // при изменении информации об спортсмене, путём долгого нажатия,
			// становится истиной. Если она true, то диалога изменения не будет.
	private String oldAthleteName; // При изменении имени, надо запомнить старое. Считаю, что имя - ключ.
	private EditText nameTextEdit; // Поле для ввода имени.
	private Spinner sexSpinner; // Спиннер для выбора пола.
	private String[] sexArray; // Массив полов.
	private EditText weightTextEdit; // Поле для ввода веса.
	private EditText heightTextEdit; // Поле для ввода роста.
	private Spinner sp; // Спиннер для выбора соревнования.

	private LinearLayout linearLayout; // Элемент, который хранит список спортсменов, отображаемый в данный момент.
	private CountryApplication countryApplication;
	private AuthorizationData authorizationData;
	private Intent askForWaitActivityIntent;
	private ArrayList<LinearLayout> linearLayoutArrayList; // Массив LinearLayout-ов. Каждому эл-ту
			// соответствует спорт. При смене вида спорта происходит смена Layout-а, и, соответственно,
			// меняется отображаемый список спортсменов.

	private TextView athleteCompetitionNumber;
	private int[] athleteNumberList; // Список, содржащий количество спортсменов на каждое соревнование, которое страна может подать.
	private String[] competitionNamesList; // Список названий соревнований.
	private CompetitionList competitionList; // Список соревнований и атлетов.
			// Этот список будет передаваться серверу.
			// Спортсмены, хранятся в этом списке, отображаются
			// в таблице на экране и в списве в одном и том же порядке.

	private Random random = new Random(); // Для генерации случайных цветов.
}