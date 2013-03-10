package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

// GUI авторизации
public class AuthorizationActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorizathion);

        Button sing_in_button = (Button) findViewById(R.id.sing_in_button);
        sing_in_button.setOnClickListener(this);
    }

    public void onLogin(boolean result) {
	    if (result){
            Toast.makeText(this, "successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "incorrect login or password", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sing_in_button:
                // процедура авторизации
                String login = ((EditText) findViewById(R.id.login_editText)).getText().toString();
                String password = ((EditText) findViewById(R.id.password_editText)).getText().toString();
                try {
                    LoginTask loginTask = new LoginTask(login, password, new URL("http://10.0.2.2:8888"), this);
                    loginTask.execute();
                } catch (MalformedURLException e) {
                    Toast.makeText(this,"fail",Toast.LENGTH_LONG).show();
                }

                /*
                //передаём данные авторизации
                Intent intent = new Intent();
                intent.putExtra("authorizationData", "ok");
                setResult(RESULT_OK, intent);
                */

                //выходим из этого активити
                this.finish();

                break;
        }
    }
}