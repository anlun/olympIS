package com.example.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.URL;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}
