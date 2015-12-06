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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager manager;
    private TextView textView;
    private ImageButton alarmButton, settingButton;

    private Vector3 previousAccelerometer;
    private Vector3 currentAccelerometer;
    private Vector3 deltaAccelerometer;

    static SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
        mydb = hlpr.getWritableDatabase();

        textView = (TextView) findViewById(R.id.text_view);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

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


        //Analyze.javaのメソッドを呼び出してテストする
        Log.d(".", "call analyze");
        mydb = hlpr.getReadableDatabase();
        //sleepCheckで取得した寝た時間をデータベースに登録


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Listenerの登録
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            previousAccelerometer = currentAccelerometer;
            currentAccelerometer = new Vector3(
                    event.values[SensorManager.DATA_X],
                    event.values[SensorManager.DATA_Y],
                    event.values[SensorManager.DATA_Z]
            );

            if (previousAccelerometer == null) {
                deltaAccelerometer = Vector3.ZERO;
            } else {
                deltaAccelerometer = new Vector3();
                deltaAccelerometer.set(currentAccelerometer);
                deltaAccelerometer.subtract(previousAccelerometer);
            }

            String str = String.format("xyz: %f %f %f\n dx dy dz: %f %f %f",
                    currentAccelerometer.x, currentAccelerometer.y, currentAccelerometer.z,
                    deltaAccelerometer.x, deltaAccelerometer.y, deltaAccelerometer.z
            );

            ContentValues values = new ContentValues();
            values.put("x", currentAccelerometer.x);
            values.put("y", currentAccelerometer.y);
            values.put("z", currentAccelerometer.z);
            values.put("dx", deltaAccelerometer.x);
            values.put("dy", deltaAccelerometer.y);
            values.put("dz", deltaAccelerometer.z);
            mydb.insert("accelerations", null, values);
            Log.d("insert accelerations", "");

            //textView.setText(str);
            if(Analyze.cnt == 0 && !Analyze.getsleeping()) {
                Analyze.calendar2 = Calendar.getInstance();
                Analyze.calendar2.add(Analyze.calendar2.SECOND, -30);//
                //calendar2.add(calendar2.DATE, -5);
                Analyze.startTime = Analyze.sdf.format(Analyze.calendar2.getTime());
                Log.d("test", Analyze.startTime);
                if (Analyze.sleepCheck("timestamp > ?", new String[]{Analyze.startTime}) && !Analyze.getsleeping()) {
                    //Analyze.writeDB("sleeptime", "timestamp", startTime);
                    Log.d("test", "awake!");
                    Analyze.sleep();
                } else {
                    Log.d("test", "sleeping!");
                }
                Analyze.cnt++;
            }else{
                if(Analyze.cnt==200){Analyze.awake();}//debug
                textView.setText(Analyze.showsleeping);
                Analyze.cnt++;
                Analyze.cnt %= 300;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
}
