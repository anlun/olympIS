package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

		Button btn = new Button(getBaseContext());
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					//new LoginTask("RUSSIA", "12345", new URL("http://10.0.2.2:8888")).execute();
					ApplicationConstrainTask app
							= new ApplicationConstrainTask("RUSLAND","RUSSIA", "12345", new URL("http://10.0.2.2:8888"));
					app.execute();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		setContentView(btn);
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
				Client cl = new Client(new URL("http://10.0.2.2:8888"));
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

	// обработка нажатий
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()){
			case 1://"log in" item
				Intent intent = new Intent(this, AuthorizationActivity.class);
				startActivityForResult(intent, 11);
				break;
			case 2://CountryGUI item.
				//проверка на авторизованность
				//if (isAuthorized) {
					Intent intent1 = new Intent(this, CountryGUI.class);
					startActivity(intent1);
				//} else {
				//	Toast.makeText(this, "you must be authorized to use this option", Toast.LENGTH_LONG).show();
				//}
				break;
			case 3://запускаем календарь
				Intent intent2 = new Intent(this, CalendarActivity.class);
				startActivity(intent2);
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
