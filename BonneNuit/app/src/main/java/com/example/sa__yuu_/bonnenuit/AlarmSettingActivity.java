package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class AlarmSettingActivity extends Activity {

    private Button deleteButton, setButton;
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
        deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(onDeleteButtonClick);

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        //timePicker.setHour(status.mHour);
        //timePicker.setMinute(status.mMinute);
        timePicker.setCurrentHour(status.mHour);
        timePicker.setCurrentMinute(status.mMinute);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alarms) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onSetButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ContentValues values = new ContentValues();
            values.put("enable", status.enable);
            values.put("hour", status.mHour);
            values.put("minute", status.mMinute);

            if (status.getId() != -1) {
                mydb.update("alarms", values, "_id = ?", new String[]{String.format("%d", status.getId())});
            } else {
                mydb.insert("alarms", null, values);
            }

            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
            startActivity(intent);
        }

    };

    private View.OnClickListener onDeleteButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
            startActivity(intent);
        }

    };
}
