package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.*;

public class AlarmActivity extends Activity {
    private int mHour, mMinute;

    static final int TIME_DIALOG_ID = 0;
    ListView listView;
    ArrayList<AlarmStatus> alarmSettings;
    AlarmListAdapter adapter;
    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        listView = (ListView)findViewById(R.id.list_view);
        alarmSettings = new ArrayList<>();

        Cursor cursor = mydb.query("alarms", new String[]{"_id", "enable", "hour", "minute"}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int enable_int = cursor.getInt(cursor.getColumnIndex("enable"));
                int hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute = cursor.getInt(cursor.getColumnIndex("minute"));
                alarmSettings.add(new AlarmStatus(id, enable_int != 0, hour, minute));
            } while (cursor.moveToNext());
        }

        adapter = new AlarmListAdapter(this);
        adapter.setAlarmList(alarmSettings);
        listView.setAdapter(adapter);

        // get the current time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // アイテムクリック時のイベント（アラームの ON/OFF）
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("position", "" + position);

                // 選択アイテムを取得
                AlarmStatus clickedStatus = (AlarmStatus) adapter.getItem(position);
                clickedStatus.toggle();
                adapter.notifyDataSetChanged();

            }
        });

        // アイテム長押し時のイベント（設定の変更・削除）
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                AlarmStatus clickedStatus = (AlarmStatus) adapter.getItem(position);
                intent.putExtra("clickedStatus", clickedStatus);
                intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmSettingActivity");
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_alarm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Sub 画面を起動

        //alarmSettings.add(new AlarmStatus(true, 10, 20));
        //adapter.notifyDataSetChanged();

        Intent intent = new Intent();
        AlarmStatus clickedStatus = new AlarmStatus(true, 7, 30);
        intent.putExtra("clickedStatus", clickedStatus);
        intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmSettingActivity");
        startActivity(intent);

        return true;
    }
}