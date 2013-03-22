package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.client.exceptions.XMLgenerationException;
import java.net.URL;

// GUI авторизации
/**
 * Class realizes the authorization GUI.
 * @author danya
 */
public class AuthorizationActivity extends Activity implements View.OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autorizathion);

		Button sing_in_button = (Button) findViewById(R.id.sing_in_button);
		sing_in_button.setOnClickListener(this);
	}

	/**
	 * To do after that the answer come from server
	 * @param result boolean. Is authorization successful on not.
	 */
	public void onLogin(boolean result) {
		if (result){
			//Toast.makeText(this, "successful", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "incorrect login or password", Toast.LENGTH_LONG).show();
		}

		//передаём данные авторизации и выходим
		Intent intent = new Intent();
		intent.putExtra("isAuthorized", result);
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	public void onClick(View v) {
		switch (v.getId()){
			case R.id.sing_in_button: // процедура авторизации
				String login = ((EditText) findViewById(R.id.login_editText)).getText().toString();
				String password = ((EditText) findViewById(R.id.password_editText)).getText().toString();

				// заполняем данные авторизации( AuthorizationData )
				AuthorizationData data = AuthorizationData.getInstance();
				data.setLogin(login);
				data.setPassword(password);

				try {
					LoginTask loginTask = new LoginTask(login, password, new URL("http://10.0.2.2:8888"), this);
					loginTask.execute();
				} catch (Exception e) {
					Toast.makeText(this,"fail",Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
}