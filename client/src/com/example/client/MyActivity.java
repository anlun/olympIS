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

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create the text view
        /*
		TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("Hello world!");

        setContentView(textView);
        */

		Button btn = new Button(getBaseContext());
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					new LoginTask("RUSSIA", "12345", new URL("http://10.0.2.2:8888")).execute();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		setContentView(btn);

		//new ConnectTask(textView).execute("10");
    }

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
        menu.add(0, 1, 0, "lig in");
        menu.add(0, 2, 0, "CountryGUI");
        menu.add(0, 3, 0, "exit");
        menu.add(0, 4, 0, "calendar");

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

        //StringBuilder sb = new StringBuilder();
        // Выведем в TextView информацию о нажатом пункте меню
        /*sb.append("Item Menu");
        sb.append("\r\n groupId: " + String.valueOf(item.getGroupId()));
        sb.append("\r\n itemId: " + String.valueOf(item.getItemId()));
        sb.append("\r\n order: " + String.valueOf(item.getOrder()));
        sb.append("\r\n title: " + item.getTitle());
        text1.setText(sb.toString());
        */

        switch (item.getItemId()){
            case 1://"log in" item
                Intent intent = new Intent(this, AuthorizationActivity.class);
                startActivityForResult(intent, 1);
                //параметры: Intent и requestCode. requestCode – необходим для идентификации.


                //Intent intent = new Intent(this, AuthorizationActivity.class);
                //startActivity(intent);
                break;
            case 2://CountryGUI item.
                //тут должна быть проверка на авторизованность
                Intent intent1 = new Intent(this, CountryGUI.class);
                startActivity(intent1);
                break;
            case 3://exit
                System.exit(0);
                break;
            case 4://запускаем календарь
                Intent intent2 = new Intent(this, CalendarActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
requestCode – тот же идентификатор, что и в startActivityForResult. По нему определяем, с какого Activity пришел результат. 
resultCode – код возврата. Определяет успешно прошел вызов или нет.
data – Intent, в котором возвращаются данные
  */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String name = data.getStringExtra("authorizationData");
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
    }
}
