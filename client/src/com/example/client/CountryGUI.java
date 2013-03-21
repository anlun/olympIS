package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;
import beans.CountryApplication;

import java.util.ArrayList;

/**
 * Class realize completing an application GUI for authorized country.
 *  @author danya
 */
public class CountryGUI extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postrequest);
        //а тут надо в табличку добавить уже имеющихся спортсменов

		athleteList = new ArrayList<Pair<String, String>>();
        text1 = (EditText)findViewById(R.id.text1);

        text2 = (EditText)findViewById(R.id.text2);

        addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

		sp = (Spinner) findViewById(R.id.competitionSpinner);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // пункт для отправки заявки на сервер
        menu.add(0, 1, 0, "post application");
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
			case 1:// post application
				AuthorizationData data = AuthorizationData.getInstance();
				countryApplication = new CountryApplication(data.getLogin(), data.getPassword(), athleteList);
				//TODO: дописать передачу countryApplication через Тошин класс
				break;
		}
		return super.onOptionsItemSelected(item);
    }

	/**
	 * Adds dynamically a row in a table.
	 * @param str1 is a String, which will be set in first column
	 * @param str2 is a String, which will be set in second column
	 */
    public void addRow(String str1, String str2) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table1);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow ll = (TableRow) inflater.inflate(R.layout.tablerow, null);
        //тут надо столько TextView обработать, сколько в таблице столбцов
        //а так же добавить спортсмена в базу

        TextView tv = (TextView) ll.getChildAt(0);
        tv.setText(str1);
        tv = (TextView) ll.getChildAt(1);
        tv.setText(str2);

        tableLayout.addView(ll);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
				// получение данных из формы и занесение в таблицу
                String[] choose = getResources().getStringArray(R.array.sport_array);
                addRow(text1.getText() + "", choose[sp.getSelectedItemPosition()]);

				// добавляем данные в список, который будем передавать
				athleteList.add(new Pair<String, String>(text1.getText() + "", choose[sp.getSelectedItemPosition()]));

                text1.setText("");
                text2.setText("не важно =)");
                Toast.makeText(this, "Спортсмен добавлен", Toast.LENGTH_SHORT).show();
                break;
        }
    }

	private Button addButton;
	private EditText text1;
	private EditText text2;
	private Spinner sp;

	private CountryApplication countryApplication;
	private ArrayList<Pair<String, String>> athleteList;
}