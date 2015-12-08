package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.ValueFormatterHelper;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends Activity {
    LineChartView chart;
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

        chart = (LineChartView) findViewById(R.id.chart);
        textView = (TextView) findViewById(R.id.text_view);
        alarmButton = (ImageButton) findViewById(R.id.alarm_button);
        settingButton = (ImageButton) findViewById(R.id.setting_button);

        alarmButton.setOnClickListener(onAlarmButtonClick);
        settingButton.setOnClickListener(onSettingButtonClick);

        Intent intent = new Intent(this, SensorService.class);
        //stopService(intent);
        startService(intent);

        // SensorService が起動していなかったら起動
        if(isServiceRunning(this, SensorService.class)) {
            Log.d("INFO", "SensorService は既に起動済です。");
        } else {
            Log.d("INFO", "SensorService を起動します。");
        }

        Calendar changeDate = new GregorianCalendar();
        // 翌日の 01:00 に繰り返す
        changeDate.set(changeDate.get(Calendar.YEAR), changeDate.get(Calendar.MONTH), changeDate.get(Calendar.DAY_OF_MONTH), 01, 00);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent setAlarmServiceIntent = new Intent(getBaseContext(), SetAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0, setAlarmServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, changeDate.getTimeInMillis(), AlarmManager.INTERVAL_DAY,  pendingIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // accelerations に値が保存させているかどうかを見るよう
        getAccelerations();
        drawGrapth();
    }

    private void drawGrapth() {
        int i = 0;
        List<PointValue> yValues = new ArrayList<PointValue>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        Cursor cursor = mydb.rawQuery("SELECT delta_length, timestamp FROM accelerations ORDER BY _id DESC LIMIT 60*10", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                float delta_length = cursor.getFloat(cursor.getColumnIndex("delta_length"));
                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("timestamp")));
                Date date = new Date(timestamp.getTime());

                yValues.add(new PointValue(-i, delta_length));
                AxisValue axisValue = new AxisValue(-i);
                axisValue.setLabel(String.format("%02d:%02d", date.getHours(), date.getMinutes()));
                axisValues.add(axisValue);

                i++;
            } while (cursor.moveToNext());
        }

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(yValues).setColor(Color.BLUE).setCubic(true).setStrokeWidth(5).setFilled(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(new Axis(axisValues).setName("Time"));
        data.setAxisYLeft(new Axis().setName("Awakeness"));

        chart.setLineChartData(data);

        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.top = 10; //example max value
        v.bottom = 0;  //example min value
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        //Optional step: disable viewport recalculations, thanks to this animations will not change viewport automatically.
        chart.setViewportCalculationEnabled(false);
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
        Cursor cursor = mydb.rawQuery("SELECT _id, x, y, z, delta_length, timestamp FROM accelerations ORDER BY _id DESC LIMIT 5", new String[]{});
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
