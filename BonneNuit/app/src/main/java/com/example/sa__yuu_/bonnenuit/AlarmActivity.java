package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.*;

public class AlarmActivity extends Activity {
    private int mHour, mMinute;

    static final int TIME_DIALOG_ID = 0;
    ListView listView;
    ArrayList<AlarmStatus> alarmSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        listView = (ListView)findViewById(R.id.list_view);
        alarmSettings = new ArrayList<>();
        alarmSettings.add(new AlarmStatus(true, Calendar.getInstance()));
        alarmSettings.add(new AlarmStatus(false, Calendar.getInstance()));
        alarmSettings.add(new AlarmStatus(true, Calendar.getInstance()));
        alarmSettings.add(new AlarmStatus(false, Calendar.getInstance()));
        alarmSettings.add(new AlarmStatus(true, Calendar.getInstance()));
        alarmSettings.add(new AlarmStatus(true, Calendar.getInstance()));
        alarmSettings.add(new AlarmStatus(true, Calendar.getInstance()));
        final AlarmListAdapter adapter = new AlarmListAdapter(this);
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
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_DARK, mTimeSetListener, mHour, mMinute, false);
        }
        return null;
    }

    // the callback received when the user "sets" the time in the dialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                }
            };
}

class AlarmStatus implements Serializable {
    boolean enable;
    long id;
    Calendar calendar;

    public AlarmStatus(boolean enable, Calendar calendar) {
        this.enable = enable;
        this.calendar = calendar;
    }

    public boolean getEnable() {
        return enable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void toggle() { enable = !enable; }

    public String getAlarmTime() {
        return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
