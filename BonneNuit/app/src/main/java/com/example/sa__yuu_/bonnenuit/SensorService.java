package com.example.sa__yuu_.bonnenuit;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class SensorService extends Service implements SensorEventListener {
    static final String TAG="SensorService";
    private int startId;

    private SensorManager manager;

    private Vector3 previousAccelerometer;
    private Vector3 currentAccelerometer;
    private Vector3 deltaAccelerometer;

    static SQLiteDatabase mydb;

    public SensorService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        Toast.makeText(this, "SensorService#onCreate", Toast.LENGTH_SHORT).show();

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(this);
        mydb = hlpr.getWritableDatabase();

        startSensor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand Received start id " + startId + ": " + intent);
        Toast.makeText(this, "SensorService#onStartCommand", Toast.LENGTH_SHORT).show();
        this.startId = startId;

        //明示的にサービスの起動、停止が決められる場合の返り値
        return START_STICKY;
    }

    private void startSensor() {
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            Sensor s = sensors.get(0);
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void stopSensor() {
        manager.unregisterListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

            ContentValues values = new ContentValues();
            values.put("x", currentAccelerometer.x);
            values.put("y", currentAccelerometer.y);
            values.put("z", currentAccelerometer.z);
            values.put("delta_length", deltaAccelerometer.length());
            mydb.insert("accelerations", null, values);

            String str = String.format("[%d] xyz: %f %f %f delta_length: %f",
                    startId,
                    currentAccelerometer.x, currentAccelerometer.y, currentAccelerometer.z,
                    deltaAccelerometer.length()
            );
            Log.d("insert accelerations", str);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
