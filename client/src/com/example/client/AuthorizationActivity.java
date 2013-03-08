package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// GUI авторизации
public class AuthorizationActivity extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorizathion);

        Button sing_in_button = (Button) findViewById(R.id.sing_in_button);
        sing_in_button.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sing_in_button:
                //должна быть процедура авторизации

                //передаём данные авторизации
                EditText login = (EditText) findViewById(R.id.login_editText);
                EditText password = (EditText) findViewById(R.id.password_editText);
                Intent intent = new Intent();
                intent.putExtra("authorizationData", "login:" + login.getText().toString() + "\npassword:" + password.getText().toString());
                setResult(RESULT_OK, intent);

                //выходим из этого активити
                this.finish();

                break;
        }
    }
}