package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import java.util.*;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager manager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.text_view);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date d3 = calendar.getTime();


        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Sub 画面を起動
        Intent intent = new Intent();
        intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
        startActivity(intent);
        
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Listenerの登録
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            String str = "加速度センサー値:"
            + "\nX軸:" + event.values[SensorManager.DATA_X]
            + "\nY軸:" + event.values[SensorManager.DATA_Y]
            + "\nZ軸:" + event.values[SensorManager.DATA_Z];
            //textView.setText(str);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
