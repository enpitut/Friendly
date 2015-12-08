package com.example.sa__yuu_.bonnenuit;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SetAlarmService extends IntentService {
    static final String TAG = "SetAlarmService";
    static SQLiteDatabase mydb;
    Handler mHandler;

    public SetAlarmService()
    {
        super("SetAlarmService");
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "#onHandleIntent");

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(this);
        mydb = hlpr.getWritableDatabase();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getBaseContext(), AlarmNotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);

        Calendar now = new GregorianCalendar();
        AlarmStatus status = AlarmStatus.findByDayOfWeek(mydb, now.get(Calendar.DAY_OF_WEEK));
        if (!status.enable) {
            Log.d(TAG, String.format("%s は enable = false な設定でした"));
            return;
        }

        if (now.get(Calendar.DAY_OF_WEEK) != status.mDayOfWeek) {
            Log.d(TAG, "曜日が違いました");
            return;
        }

        GregorianCalendar alarmTarget = new GregorianCalendar();
        alarmTarget.set(
                alarmTarget.get(now.YEAR),
                alarmTarget.get(now.MONTH),
                alarmTarget.get(now.DAY_OF_MONTH),
                status.mHour,
                status.mMinute,
                0
        );
        if (alarmTarget.before(now)) {
            Log.d(TAG, "時間が過ぎていました");
            return;
        }

        Log.d(TAG, String.format("アラームを設定しました: %s", alarmTarget));
        String message = String.format("アラームを設定しました: %d/%d %d:%d",
                alarmTarget.get(Calendar.MONTH) + 1,
                alarmTarget.get(Calendar.DAY_OF_MONTH),
                alarmTarget.get(Calendar.HOUR_OF_DAY),
                alarmTarget.get(Calendar.MINUTE));
        mHandler.post(new DisplayToast(this, message));
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTarget.getTimeInMillis(), pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
