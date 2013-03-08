package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

/*
Класс-activity. Содержит общее рассписание
*/

public class CalendarActivity extends Activity implements OnItemSelectedListener, OnClickListener {

    //все фильтры(spinner), которые будут
    private Spinner countrySpinner;
    private Spinner sportsSpinner;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        //инициализируем спиннеры
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        sportsSpinner = (Spinner) findViewById(R.id.sportsSpinner);

        //устанавливаем листенеры на спинеры
        countrySpinner.setOnItemSelectedListener(this);
        sportsSpinner.setOnItemSelectedListener(this);

        //устанавливаем onClickListener для всех дней
        LinearLayout ll = (LinearLayout) findViewById(R.id.mainLayout);
        for(Integer j = 1; j < 4 ; j++)  {
            LinearLayout ll1 = (LinearLayout) ll.getChildAt(j);
            for(Integer i = 0; i < 7 ; i++ ){
                TextView tv = (TextView) ll1.getChildAt(i);
                tv.setOnClickListener(this);
            }
        }
    }


    //выбор на спиннере
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int selectedItemPosition, long id) {
        Toast.makeText(getApplicationContext(), "Ваш выбор: " + parent.getItemAtPosition(selectedItemPosition).toString(), Toast.LENGTH_SHORT).show();

        //применить выбранный фильтр
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    // клик по дню
    @Override
    public void onClick(View view){
        //отрываем день
        Intent intent = new Intent(this, DayActivity.class);
        startActivity(intent);
    }


}
