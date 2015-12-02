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

        Intent intent = new Intent();
        intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
        startActivity(intent);

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

            // アプリを起動して10秒後にインテントを発動する
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(
                    calendar.get(GregorianCalendar.YEAR),
                    calendar.get(GregorianCalendar.MONTH),
                    calendar.get(GregorianCalendar.DAY_OF_MONTH),
                    status.mHour,
                    status.mMinute,
                    0
            );

            Log.d("calendar", calendar.toString());

            AlarmManager mng = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent();
            alarmIntent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.MainActivity");
            // TODO: 音を鳴らすActivityを作る
            PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, alarmIntent, 0);
            mng.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

            // 下は一日ごとの繰り返しをするための設定だが、キャンセル処理が分からないのでコメントアウト
            //mng.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
            startActivity(intent);
        }

    };
}
