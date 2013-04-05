package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import beans.CompetitionList;
import beans.CountryApplication;
import java.net.URL;

/**
 * Just test activity.
 * @author Podkopaev Anton vs danya
 */
public class MyActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		isAuthorized = false;
	}

	/**
	 * Just test of connecting to server task.
	 */
	class ConnectTask extends AsyncTask<String, Integer, String> {
		public ConnectTask(TextView textView) {
			this.textView = textView;
		}

		@Override
		protected String doInBackground(String... cmds) {
			String result = "Fail";
			try {
				Client cl = new Client(new URL("http://178.130.32.141:8888"));
				result = cl.execute(cmds[0]);
			} catch (Exception e) {
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			textView.setText(result);
		}

		private TextView textView;
	}

	/* создание меню*/
	public boolean onCreateOptionsMenu(Menu menu) {
		//1-ый пункт - ID группы
		menu.add(0, 1, 0, "log in");
		menu.add(0, 2, 0, "Country Application");
		menu.add(0, 3, 0, "calendar");
		menu.add(0, 4, 0, "exit");

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
			case 1://"log in" item
				//проверка на авторизованность, если уже авторизован - не авторизуем заново.
				if (isAuthorized) {
					Toast.makeText(this, "you are already authorized", Toast.LENGTH_LONG).show();
					break;
				} else {
					startActivityForResult(new Intent(this, AuthorizationActivity.class), 11);
				}
				break;
			case 2://CountryGUI item.
				//проверка на авторизованность
				if (isAuthorized) {
					startActivity(new Intent(this, CountryGUI.class));
				} else {
					Toast.makeText(this, "you must be authorized to use this option", Toast.LENGTH_LONG).show();
				}
				break;
			case 3://запускаем календарь
				startActivity(new Intent(this, CalendarActivity.class));
				break;
			case 4://exit
				System.exit(0);
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
		if (intentData == null) {return;}
		if (requestCode == 11 && resultCode == RESULT_OK) {
			isAuthorized = intentData.getBooleanExtra("isAuthorized", false);
			if (isAuthorized){
				Toast.makeText(this, "you are singed in", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "authorization failed", Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean isAuthorized;
}
