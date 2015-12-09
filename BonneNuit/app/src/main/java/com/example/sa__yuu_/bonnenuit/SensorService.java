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

import java.util.Calendar;
import java.util.List;

public class SensorService extends Service implements SensorEventListener {
    static final String TAG = "SensorService";
    private int startId;

    private SensorManager manager;

    private Calendar nextInsert;
    private Vector3 previousAccelerometer;
    private Vector3 currentAccelerometer;
    private Vector3 deltaAccelerometer;

    private float sumDeltaLength = 0f;
    private float maxDeltaLength = 0f;
    private float minDeltaLength = 0f;
    private int countDeltaLength = 0;

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
        nextInsert = Calendar.getInstance();
        nextInsert.add(Calendar.SECOND, 60);
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
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

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

        Calendar now = Calendar.getInstance();
        if (now.after(nextInsert)) {
            ContentValues values = new ContentValues();
            values.put("x", currentAccelerometer.x);
            values.put("y", currentAccelerometer.y);
            values.put("z", currentAccelerometer.z);
            values.put("avg", sumDeltaLength / countDeltaLength);
            values.put("max", maxDeltaLength);
            values.put("min", minDeltaLength);
            mydb.insert("accelerations", null, values);

            String str = String.format("[%d] xyz: %f %f %f | %f %f %f",
                    startId,
                    currentAccelerometer.x, currentAccelerometer.y, currentAccelerometer.z,
                    sumDeltaLength / countDeltaLength, maxDeltaLength, maxDeltaLength
            );
            Log.d("insert accelerations", str);

            nextInsert = Calendar.getInstance();
            nextInsert.add(Calendar.SECOND, 60);

            sumDeltaLength = 0f;
            minDeltaLength = 1000f;
            maxDeltaLength = 0f;
            countDeltaLength = 0;
        } else {
            float length = deltaAccelerometer.length();

            sumDeltaLength += length;

            if(length > maxDeltaLength) {
                maxDeltaLength = length;
            }

            if(length < minDeltaLength) {
                minDeltaLength = length;
            }

            countDeltaLength++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
