package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class realizes the authorization GUI.
 * @author danya
 */
public class AuthorizationActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autorizathion);

		(findViewById(R.id.sing_in_button)).setOnClickListener(this);
	}

	/**
	 * To do after that the answer come from server.
	 * @param result boolean. Is authorization successful or not.
	 */
	public void onLogin(boolean result) {
		if (result){
			Log.d("DAN", "CalendarActivity authorization ok.");
		} else {
			Log.d("DAN", "CalendarActivity authorization fail.");
		}

		//передаём данные авторизации и выходим
		Intent intent = new Intent();
		intent.putExtra("isAuthorized", result);
		this.setResult(RESULT_OK, intent);
		finishActivity(10);
		this.finish();
	}

	public void onClick(View v) {
		switch (v.getId()){
			case R.id.sing_in_button: // процедура авторизации
				// вешаем гуи
				startActivityForResult(new Intent(this, AskForWaitActivity.class), 10);

				String login = ((EditText) findViewById(R.id.login_editText)).getText().toString();
				String password = ((EditText) findViewById(R.id.password_editText)).getText().toString();

				// заполняем данные авторизации( AuthorizationData )
				AuthorizationData data = AuthorizationData.getInstance();
				data.setLogin(login);
				data.setPassword(password);
				try {
					data.setServerURL(new URL("http://178.130.32.141:8888"));
				} catch (MalformedURLException e) {
					Log.d("DAN", "MalformedURLException in OnClick method AuthorizationActivity.java");
				}

				try {
					LoginTask loginTask = new LoginTask(login, password, new URL("http://178.130.32.141:8888"), this);
					loginTask.execute();
				} catch (Exception e) {
					Toast.makeText(this,"fail",Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
}