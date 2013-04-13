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

		// Вешаем гуи пока не дождемся ответа от сервера, запуская AskForWaitActivity.
		startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);

		sexArray = getResources().getStringArray(R.array.sex_array);
		(findViewById(R.id.competitionListViewButton)).setOnClickListener(this);
		(findViewById(R.id.deleteAthleteButton)).setOnClickListener(this);
		forceEdit = false;
		oldAthleteName = "";
		currentCompetition = "all competitions";
		nameTextEdit = (EditText)findViewById(R.id.nameTextEdit);
		sexSpinner = (Spinner) findViewById(R.id.sexSpinner);
		weightTextEdit = (EditText)findViewById(R.id.weightTextEdit);
		heightTextEdit = (EditText)findViewById(R.id.heightTextEdit);
		linearLayout = (LinearLayout) findViewById(R.id.linLayMain);
		selectedSports = new ArrayList<String>();
		athleteListView = new ArrayList<ClientAthlete>();
		findViewById(R.id.add_button).setOnClickListener(this);
		athleteCompetitionNumber = (TextView) findViewById(R.id.athleteCompetitionNumber);
		sportSpinner = (Spinner) findViewById(R.id.competitionSpinner);

		AuthorizationData authorizationData = AuthorizationData.getInstance();
		(new ExistApplicationGetTask(authorizationData.getLogin(), authorizationData.getPassword(),
				authorizationData.getServerURL(), this)).execute();
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

	// обработка нажатий меню
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case 1:// post application
				// Вешаем гуи пока не дождемся ответа от сервера, запуская AskForWaitActivity.
				startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);

				ArrayList<ClientCompetition> arcl = new ArrayList<ClientCompetition>();
				for (int i = 0; i < competitionNamesList.length; i++) {
					String competition = competitionNamesList[i];
					ClientCompetition clientCompetition = new ClientCompetition(competition, athleteMaxNumberList[i]);
					for (ClientAthlete clientAthlete : athleteListView) {
						if (clientAthlete.getCompetitions().contains(competition)) {
							clientCompetition.addAthlete(0, new Athlete(
									clientAthlete.getName(), clientAthlete.getSex(),
									clientAthlete.getWeight(), clientAthlete.getHeight(),
									competition
							));
						}
					}
					arcl.add(clientCompetition);
				}
				CompetitionList competitionList = new CompetitionList();
				competitionList.setCompetitionList(arcl);
				AuthorizationData data = AuthorizationData.getInstance();
				CountryApplication countryApplication = new CountryApplication(data.getLogin(), data.getPassword(), competitionList);

				(new ApplicationSendTask(countryApplication, data.getServerURL(), this)).execute();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void getCountryApplicationFromServer(CountryApplication result) {
		Log.d("DAN", "получили ответ от сервера. Запустился getCountryApplicationFromServer.");
		ArrayList<ClientCompetition>  compList = result.getCompetitionList().getCompetitionList();

		if (compList.size() != 0) {
			Log.d("DAN", "получили не пустую заявку.");
			// заполнить layout-ы спортсменами
			athleteMaxNumberList = new int[compList.size()];
			athleteCurrentNumberList = new int[compList.size()];
			competitionNamesList = new String[compList.size()];
			sportSpinnerCompetitionsNameList = new String[compList.size() + 1];
			sportSpinnerCompetitionsNameList[0] = "all competitions";
			int i = 0;
			for (ClientCompetition competition: compList) {
				Log.d("DAN", "competition." + competition.getCompetition());
				athleteMaxNumberList[i] = competition.getMaxAthleteNumber(); // Список, содржащий количество спортсменов на каждое соревнование, которое страна может подать.
				competitionNamesList[i] = competition.getCompetition(); // Список названий соревнований.
				athleteCurrentNumberList[i] = 0;
				sportSpinnerCompetitionsNameList[i + 1] = competition.getCompetition(); // Список названий соревнований в спиннере.
				i++;

				// NEW забиваем список спортсменами
				for (Athlete athlete : competition.getAthleteCompetitionList()) {
					Log.d("DAN", "addAthlete " + athlete.getName() );
					addAthleteToAthleteListView(athlete);
				}
			}

			//показываем построенный список спортсменов пользователю
			for (ClientAthlete athlete : athleteListView) {
				Log.d("DAN", "addRow " + athlete.getName());
				addRow(athlete.getName());
			}

			// меняем число человек в заявке
			for (ClientAthlete athlete : athleteListView) {
				for(String competition : athlete.getCompetitions()) {
					for (int w = 0; w < compList.size(); w++) {
						if (competitionNamesList[w].equals(competition)) {
							if (athleteMaxNumberList[w] == athleteCurrentNumberList[w]) {
								Toast.makeText(this, "fail!incorrect country application.", Toast.LENGTH_LONG).show();
								finishActivity(10);
								this.finish();
								return;
							}
							athleteCurrentNumberList[w]++;
							w = compList.size();
						}
					}
				}
			}

			currentCompetition = "all competitions";
			sportSpinner.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, sportSpinnerCompetitionsNameList));
			sportSpinner.setSelection(0);
			sportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				// Вызывается при смене Item в спиннере.
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int selectedItem, long id) {
					// очищаем все, нужно, чтобы не было фэйлов в работе
					forceEdit = false;
					oldAthleteName = "";
					nameTextEdit.setText("");
					sexSpinner.setSelection(0);
					weightTextEdit.setText("");
					heightTextEdit.setText("");
					selectedSports = new ArrayList<String>();

					currentCompetition = sportSpinnerCompetitionsNameList[selectedItem];
					if (selectedItem > 0 && selectedItem <= competitionNamesList.length) {
						selectedItem--;
						// Выставляем число атлетов в заявке и макс.число атлетов по данному соревнованию.
						int athleteCurrentNumber = athleteCurrentNumberList[selectedItem];
						int athleteMaxNumber = athleteMaxNumberList[selectedItem];
						athleteCompetitionNumber.setText("осталось спортсменов: \n" + (athleteMaxNumber - athleteCurrentNumber) +
								"/" + athleteMaxNumber);
					} else {
						athleteCompetitionNumber.setText("Choose\ncomp..");
					}

					// меняем содержимое view для пользователя по выбранному соревнованию
					if (!currentCompetition.equals("all competitions")) {
						linearLayout.removeAllViews();
						for (ClientAthlete athlete : athleteListView) {
							if (athlete.getCompetitions().contains(currentCompetition)) {
								addRow(athlete.getName());
							}
						}
					} else {
						linearLayout.removeAllViews();
						for (ClientAthlete athlete : athleteListView) {
							addRow(athlete.getName());
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});


			// Убиваем AskForWaitActivity. 10 - requestCode этого активити.
			finishActivity(10);
			Log.d("DAN", "завершили установку данных, полученных с сервера.");
		} else {
			Log.d("DAN", "убиваем CountryGUIActivity т.к. заявка, пришедшая с базы, пуста");
			try{
				finishActivity(10);
			} catch (Exception e) {
				Log.d("DAN", "поймали exception в CountryGUI, когда сфэйлилась авторизация, скорее всего, т.к. не существует WaitActivity, если так, то норм.");
			}
			finish();
			Log.d("DAN", "убили CountryGUIActivity т.к. заявка, пришедшая с базы, пуста");
		}
	}

	// Считаю, что long click есть только у TextView каждого атлета.
	public boolean onLongClick(View v) {
		TextView tv = (TextView) v;
		String name = tv.getText() + "";
		Log.d("DAN","get athlete");
		ClientAthlete athlete = getClientAthleteFromAthleteListView(name);
		if (athlete == null) {
			return true;
		}
		Log.d("DAN","get name");
		nameTextEdit.setText(athlete.getName());
		Log.d("DAN", "get sex");
		sexSpinner.setSelection(doIndexFomSex(athlete.getSex()));
		Log.d("DAN", "get weight");
		weightTextEdit.setText(athlete.getWeight() + "");
		Log.d("DAN", "get height");
		heightTextEdit.setText(athlete.getHeight() + "");
		Log.d("DAN", "get sports " + athlete.getCompetitions().toString());
		selectedSports = athlete.getCompetitions();

		forceEdit = true;
		oldAthleteName = name;

		return true;
	}

	public void onClick(View v) {
		switch (v.getId()){
			case R.id.competitionListViewButton:
				Log.d("DAN","case R.id.competitionListViewButton:");

				Intent tableCountryFilterIntent = new Intent(this, TableFilter.class);
				tableCountryFilterIntent.putExtra("filterNumber", "selectSport");
				tableCountryFilterIntent.putExtra("resourceArray", competitionNamesList);

				// Передаём уже выбранные эл-ты.
				tableCountryFilterIntent.putExtra("filterIsAlreadySelectedItems", selectedSports);

				startActivityForResult(tableCountryFilterIntent, 1);
				break;
			case R.id.add_button:
				if (!forceEdit) {
					if (getAthleteIndexFromAthleteListView(nameTextEdit.getText() + "") != -1) {
						// Т.е. спортсмен уже есть в таблице
						Intent dialogActivity = new Intent(this, DialogActivity.class);
						// Далее вторым параметром стоит 2. Это requestCode, он может быть любым числом.
						// Выбрана 2, т.к. 1 уже использовалось в другом классе. requestCode может совпадать
						// в разных местах программы и даже в одном классе,
						// но рекомендуется ставить разные значения, во избежания неожиданных ошибок.
						startActivityForResult(dialogActivity, 2);
						break;
					}

					if (addAthlete()) {
						Toast.makeText(this, "Новый спортсмен добавлен", Toast.LENGTH_SHORT).show();
					}
				} else {
					// Имя спортсмена.
					if (addAthlete()) {
						Toast.makeText(this, "Информация изменена", Toast.LENGTH_SHORT).show();
						forceEdit = false;
						oldAthleteName = "";
					}
				}
				break;
			case R.id.deleteAthleteButton:
				if (forceEdit) {
					// Удаляем старые данные из таблицы пользователя.
					linearLayout.removeViewAt(getAthleteIndexInLinearLayout(oldAthleteName));
					// Удаляем старые данные о спортсмене
					removeAthleteFromAthleteListView(oldAthleteName);
					forceEdit = false; oldAthleteName = "";
				} else {
					Toast.makeText(this, "удалить спортсмена можно только при изменении информации о нём(долгое нажатие по имени)",
							Toast.LENGTH_LONG).show();
				}
				break;
			default: // Это значит, что это был клик по спортсмену.
				TextView tv = (TextView) v;
				String name = tv. getText() + "";
				ClientAthlete athlete = getClientAthleteFromAthleteListView(name);
				String competitions = "";
				for (String competition : athlete.getCompetitions()) {
					competitions += competition + "\n\t";
				}

				Log.d("DAN","get athleteInformation");
				String athleteInformation = "Name: " + athlete.getName() + "\n\n" +
						"Sex: " + sexToString(athlete.getSex()) + "\n\n" +
						"Weight: " + athlete.getWeight() + "\n\n" +
						"Height: " + athlete.getHeight() + "\n\n" +
						"Competitions: " + competitions;
				Log.d("DAN","new intent");
				Intent dayActivityIntent = new Intent(this, AthleteInformationActivity.class);
				dayActivityIntent.putExtra("athleteInformation", athleteInformation);
				Log.d("DAN","startActivity");
				startActivity(dayActivityIntent);
				break;
		}
	}

	public void doFinish() {
		finishActivity(10);
		this.finish();
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
					forceEdit = true;
					oldAthleteName = nameTextEdit.getText() + "";
					if (addAthlete()) {
						Toast.makeText(this, "Информация о спортсмене изменёна", Toast.LENGTH_SHORT).show();
						forceEdit = false;
						oldAthleteName = "";
					}
				}
			}
		} else if (requestCode == 1) { // т.е. мы из ListView спортов
			if (resultCode == RESULT_OK) {
				try {
					//результат выбора в ListView пользователем.
					selectedSports = data.getStringArrayListExtra("resultOfChoice");
					Log.d("DAN", "selected sports: " + selectedSports.toString());

					Toast.makeText(this, selectedSports.toString(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(this, "Неизвестная ошибка при работе с фильтрами.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	// Returns true, if athlete with this name is in the list.
	private boolean addAthleteToAthleteListView(Athlete newAthlete) {
		for (ClientAthlete athlete : athleteListView) {
			if (athlete.getName().equals(newAthlete.getName())) {
				athlete.addCompetition(newAthlete.getCompetition());
				return true;
			}
		}
		athleteListView.add(new ClientAthlete(newAthlete));
		return false;
	}

	private void removeAthleteFromAthleteListView(String name) {
		for (ClientAthlete athlete : athleteListView) {
			if (athlete.getName().equals(name)) {
				athleteListView.remove(athlete);
				return;
			}
		}
	}

	private int getAthleteIndexFromAthleteListView(String name) {
		for (int i = 0; i < athleteListView.size(); i++) {
			if (athleteListView.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private ClientAthlete getClientAthleteFromAthleteListView(String name) {
		for (ClientAthlete athlete : athleteListView) {
			if (athlete.getName().equals(name)) {
				return athlete;
			}
		}
		return null;
	}

	private int getAthleteIndexInLinearLayout(String name) {
		for ( int i = 0; i < linearLayout.getChildCount(); i++) {
			if (((TextView)linearLayout.getChildAt(i)).getText().equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	* Adds dynamically a athlete in list and visible layout.
	*/
	private void addRow(String name) {
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

		Log.d("DAN","linearLayout.addView(tv,index);");
		linearLayout.addView(tv,0);
		Log.d("DAN","exit addRow");
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
	 */
	private boolean addAthlete() {
		// Добавляем данные в список, который будем передавать.
		try {
			String name = nameTextEdit.getText() + "";
			Sex sex = toSex(sexArray[sexSpinner.getSelectedItemPosition()]);
			int weight = Integer.parseInt((weightTextEdit.getText() + ""));
			int height = Integer.parseInt((heightTextEdit.getText() + ""));
			// TODO цифры из головы
			if (weight < 20 || weight > 200) {
				Toast.makeText(this, "Вес введены некорректно. 20..200", Toast.LENGTH_SHORT).show();
				return false;
			} else if (height < 100 || height > 250){
				Toast.makeText(this, "Вес или рост введены некорректно. 100..250", Toast.LENGTH_SHORT).show();
				return false;
			}

			if(forceEdit){
				// Удаляем старые данные из таблицы пользователя.
				Log.d("DAN", "addAthlete. linearLayout.removeViewAt(getAthleteIndexInLinearLayout(name)); " +  oldAthleteName);
				linearLayout.removeViewAt(getAthleteIndexInLinearLayout(oldAthleteName));
				// Удаляем старые данные о спортсмене
				Log.d("DAN", "addAthlete. removeAthleteFromAthleteListView(oldAthleteName);" + oldAthleteName);
				removeAthleteFromAthleteListView(oldAthleteName);
				forceEdit = false;
			}
			athleteListView.add(new ClientAthlete(name, sex, weight, height, selectedSports));
			// меняем число человек в заявке
			int[] newAthleteCurrentNumberList = athleteCurrentNumberList;
			for (String comp : selectedSports) {
				for (int i = 0; i < competitionNamesList.length; i++) {
					if (competitionNamesList[i].equals(comp)) {
						if (athleteMaxNumberList[i] == newAthleteCurrentNumberList[i]) {
							Toast.makeText(this, "вы исчерпали количество заявок по соревнованию " + comp, Toast.LENGTH_SHORT).show();
							return false;
						}
						newAthleteCurrentNumberList[i]++;
						i = competitionNamesList.length;
					}
				}
			}
			athleteCurrentNumberList = newAthleteCurrentNumberList;

			// Добавляем информацию в таблицу пользователя.
			if (selectedSports.contains(currentCompetition) || currentCompetition.equals("all competitions")) {
				addRow(name);
			}

			// обновляем число участников во вьюшке
			int selectedItem = sportSpinner.getSelectedItemPosition();
			currentCompetition = sportSpinnerCompetitionsNameList[selectedItem];
			if (selectedItem > 0) {
				selectedItem--;
				// Выставляем число атлетов в заявке и макс.число атлетов по данному соревнованию.
				int athleteCurrentNumber = athleteCurrentNumberList[selectedItem];
				int athleteMaxNumber = athleteMaxNumberList[selectedItem];
				athleteCompetitionNumber.setText("Осталось: " + (athleteMaxNumber - athleteCurrentNumber) +
						"/" + athleteMaxNumber);
			} else {
				athleteCompetitionNumber.setText("Choose\ncomp..");
			}
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Вес или рост введены некорректно.", Toast.LENGTH_SHORT).show();
			return false;
		}

		nameTextEdit.setText("");
		sexSpinner.setSelection(0);
		weightTextEdit.setText(""); heightTextEdit.setText("");
		selectedSports = new ArrayList<String>();
		forceEdit = false; oldAthleteName = "";
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


	private boolean forceEdit; // при изменении информации об спортсмене, путём долгого нажатия,
			// становится истиной. Если она true, то диалога изменения не будет.
	private String oldAthleteName; // При изменении имени, надо запомнить старое. Считаю, что имя - ключ.
	private EditText nameTextEdit; // Поле для ввода имени.
	private Spinner sexSpinner; // Спиннер для выбора пола.
	private String[] sexArray; // Массив полов.
	private EditText weightTextEdit; // Поле для ввода веса.
	private EditText heightTextEdit; // Поле для ввода роста.
	private ArrayList<String> selectedSports; // NEW
	private ArrayList<ClientAthlete> athleteListView; // список спортсемнов NEW
	private Spinner sportSpinner; // для сортировки по соревнованию
	private String[] sportSpinnerCompetitionsNameList;// Список названий соревнований, первым эл-том
			// содержащий "all competitions". Для сортировки по соревнованиям.
	private String currentCompetition; // текущее соревнование, выбранное в фильтре.
	private TextView athleteCompetitionNumber;
	private LinearLayout linearLayout; // Элемент, который хранит список спортсменов, отображаемый в данный момент.
	private int[] athleteCurrentNumberList; // Список, содржащий количество спортсменов на каждое соревнование, которое уже в заявке.
	private int[] athleteMaxNumberList; // Список, содржащий количество спортсменов на каждое соревнование, которое страна может подать.
	private String[] competitionNamesList; // Список названий соревнований.
	private Random random = new Random(); // Для генерации случайных цветов.
}