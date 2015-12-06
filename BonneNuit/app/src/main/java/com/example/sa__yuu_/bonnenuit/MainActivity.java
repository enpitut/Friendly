package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private TextView textView;
    private ImageButton alarmButton, settingButton;
    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        textView = (TextView) findViewById(R.id.text_view);
        alarmButton = (ImageButton) findViewById(R.id.alarm_button);
        settingButton = (ImageButton) findViewById(R.id.setting_button);

        alarmButton.setOnClickListener(onAlarmButtonClick);
        settingButton.setOnClickListener(onSettingButtonClick);

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date d3 = calendar.getTime();


        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });
        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, DateFormat.getTimeInstance()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        Intent intent = new Intent(this, SensorService.class);
        //stopService(intent);
        startService(intent);

        // SensorService が起動していなかったら起動
        if(isServiceRunning(this, SensorService.class)) {
            Log.d("INFO", "SensorService は既に起動済です。");
        } else {
            Log.d("INFO", "SensorService を起動します。");
        }

        // accelerations に値が保存させているかどうかを見るよう
        getAccelerations();
    }

    public boolean isServiceRunning(Context c, Class<?> cls) {
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningService = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo i : runningService) {
            Log.d("", "service: " + i.service.getClassName() + " : " + i.started);
            if (cls.getName().equals(i.service.getClassName())) {
                Log.d("", "running");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private View.OnClickListener onAlarmButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
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

    private void getAccelerations() {
        Cursor cursor = mydb.rawQuery("SELECT _id, x, y, z, delta_length, timestamp FROM accelerations ORDER BY _id DESC LIMIT 50", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                float x = cursor.getFloat(cursor.getColumnIndex("x"));
                float y = cursor.getFloat(cursor.getColumnIndex("y"));
                float z = cursor.getFloat(cursor.getColumnIndex("z"));
                float delta_length = cursor.getFloat(cursor.getColumnIndex("delta_length"));
                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("timestamp")));
                Log.d("getAccelerations", String.format("%d ||%f %f %f|| = %f, %s", id, x, y, z, delta_length, timestamp));
            } while (cursor.moveToNext());
        }
    }
}
