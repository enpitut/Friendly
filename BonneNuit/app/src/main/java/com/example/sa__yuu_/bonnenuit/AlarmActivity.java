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
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.*;

public class AlarmActivity extends Activity {
    private ImageButton chartButton, settingButton;

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

        chartButton = (ImageButton) findViewById(R.id.chart_button);
        settingButton = (ImageButton) findViewById(R.id.setting_button);

        chartButton.setOnClickListener(onChartButtonClick);
        settingButton.setOnClickListener(onSettingButtonClick);

        listView = (ListView)findViewById(R.id.list_view);
        alarmSettings = new ArrayList<>();

        adapter = new AlarmListAdapter(this);
        adapter.setAlarmList(alarmSettings);
        listView.setAdapter(adapter);

        // アイテムクリック時のイベント（アラームの ON/OFF）
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("position", "" + position);

                // 選択アイテムを取得
                AlarmStatus clickedStatus = (AlarmStatus) adapter.getItem(position);
                clickedStatus.toggle();
                clickedStatus.update(mydb);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmSettings.clear();
        Cursor cursor = mydb.query("alarms", new String[]{"_id", "enable", "day_of_week", "hour", "minute"}, null, null, null, null, "_id DESC");
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int enable_int = cursor.getInt(cursor.getColumnIndex("enable"));
                int day_of_week = cursor.getInt(cursor.getColumnIndex("day_of_week"));
                int hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute = cursor.getInt(cursor.getColumnIndex("minute"));
                alarmSettings.add(new AlarmStatus(id, enable_int != 0, day_of_week, hour, minute));
            } while (cursor.moveToNext());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_alarm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private View.OnClickListener onChartButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.MainActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    private View.OnClickListener onSettingButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.SettingActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}