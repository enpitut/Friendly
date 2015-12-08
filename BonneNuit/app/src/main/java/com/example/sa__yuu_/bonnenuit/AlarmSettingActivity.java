package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmSettingActivity extends Activity {

    private Button setButton;
    private TimePicker timePicker;
    private AlarmStatus status;

    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        status = (AlarmStatus) getIntent().getSerializableExtra("clickedStatus");

        setButton = (Button) findViewById(R.id.set_button);
        setButton.setOnClickListener(onSetButtonClick);

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        //timePicker.setHour(status.mHour);
        //timePicker.setMinute(status.mMinute);
        timePicker.setCurrentHour(status.mHour);
        timePicker.setCurrentMinute(status.mMinute);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                status.mHour = hourOfDay;
                status.mMinute = minute;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (status.getId() != -1) {
            status.delete(mydb);
        }

        finish();

        return true;
    }

    private View.OnClickListener onSetButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (status.getId() != -1) {
                status.update(mydb);
            } else {
                status.insert(mydb);
            }

            Intent setAlarmServiceIntent = new Intent(getBaseContext(), SetAlarmService.class);
            startService(setAlarmServiceIntent);
            finish();
        }

    };
}
