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
import utils.Utils;

import java.net.URL;

/**
 * Just test activity.
 * @author Podkopaev Anton vs danya
 */
public class MyActivity extends Activity implements View.OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.exitButton).setOnClickListener(this);
		findViewById(R.id.logOutButton).setOnClickListener(this);
		findViewById(R.id.logOutButton).setEnabled(false);
		findViewById(R.id.signInButton).setOnClickListener(this);
		findViewById(R.id.calendarButton).setOnClickListener(this);
		findViewById(R.id.countryGUIButton).setOnClickListener(this);
		findViewById(R.id.countryGUIButton).setEnabled(false);

		isAuthorized = false;
	}

	public void onClick(View v) {
		switch (v.getId()){
			case R.id.signInButton://"log in" item
				//проверка на авторизованность, если уже авторизован - не авторизуем заново.
				if (isAuthorized) {
					Toast.makeText(this, "you are already authorized", Toast.LENGTH_LONG).show();
					break;
				} else {
					startActivityForResult(new Intent(this, AuthorizationActivity.class), 11);
				}
				break;
			case R.id.logOutButton:
				isAuthorized = false;
				AuthorizationData data = AuthorizationData.getInstance();
				data.setPassword("");
				data.setLogin("");
				Toast.makeText(this, "you are log out", Toast.LENGTH_SHORT).show();
				findViewById(R.id.logOutButton).setEnabled(false);
				findViewById(R.id.signInButton).setEnabled(true);
				findViewById(R.id.countryGUIButton).setEnabled(false);
				break;
			case R.id.countryGUIButton://CountryGUI item.
				//проверка на авторизованность
				if (isAuthorized) {
					startActivity(new Intent(this, CountryGUI.class));
				}
				break;
			case R.id.calendarButton://запускаем календарь
				startActivity(new Intent(this, CalendarActivity.class));
				break;
			case R.id.exitButton://exit
				System.exit(0);
				break;
		}
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
				Client cl = new Client(new URL(Utils.serverAddress));
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {
		if (intentData == null) {return;}
		if (requestCode == 11 && resultCode == RESULT_OK) {
			isAuthorized = intentData.getBooleanExtra("isAuthorized", false);
			if (isAuthorized){
				Toast.makeText(this, "you are singed in", Toast.LENGTH_LONG).show();

				findViewById(R.id.logOutButton).setEnabled(true);
				findViewById(R.id.signInButton).setEnabled(false);
				findViewById(R.id.countryGUIButton).setEnabled(true);
			} else {
				Toast.makeText(this, "authorization failed", Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean isAuthorized;
}
