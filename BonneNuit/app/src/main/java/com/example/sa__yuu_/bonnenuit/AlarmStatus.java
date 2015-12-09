package com.example.sa__yuu_.bonnenuit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlarmStatus implements Serializable {
    boolean enable;
    int mDayOfWeek, mHour, mMinute;
    long id;

    public AlarmStatus(boolean enable, int mDayOfWeek, int mHour, int mMinute) {
        this.id = -1;
        this.enable = enable;
        this.mDayOfWeek = mDayOfWeek;
        this.mHour = mHour;
        this.mMinute = mMinute;
    }

    public AlarmStatus(long id, boolean enable, int mDayOfWeek, int mHour, int mMinute) {
        this.id = id;
        this.enable = enable;
        this.mDayOfWeek = mDayOfWeek;
        this.mHour = mHour;
        this.mMinute = mMinute;
    }

    public boolean getEnable() {
        return enable;
    }

    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void toggle() {
        enable = !enable;
    }

    public String getAlarmTime() {
        return String.format("%02d:%02d", mHour, mMinute);
    }

    public void setAlarm() {
    }

    // Database
    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("enable", enable);
        values.put("day_of_week", mDayOfWeek);
        values.put("hour", mHour);
        values.put("minute", mMinute);
        return values;
    }

    public void insert(SQLiteDatabase db) {
        db.insert("alarms", null, getContentValues());
    }

    public void update(SQLiteDatabase db) {
        db.update("alarms", getContentValues(), "_id = ?", new String[]{String.format("%d", getId())});
    }

    public void delete(SQLiteDatabase db) {
        db.delete("alarms", "_id = ?", new String[]{String.format("%d", getId())});
    }

    public static ArrayList<AlarmStatus> getAll(SQLiteDatabase db) {
        Cursor cursor = db.query("alarms",
                new String[]{"_id", "enable", "day_of_week", "hour", "minute"},
                null, null, null, null, "_id ASC");

        ArrayList<AlarmStatus> settings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int enable_int = cursor.getInt(cursor.getColumnIndex("enable"));
                int day_of_week = cursor.getInt(cursor.getColumnIndex("day_of_week"));
                int hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute = cursor.getInt(cursor.getColumnIndex("minute"));
                settings.add(new AlarmStatus(id, enable_int != 0, day_of_week, hour, minute));
            } while (cursor.moveToNext());
        }
        return settings;
    }

    public static AlarmStatus findByDayOfWeek(SQLiteDatabase db, int dayOfWeek) {
        Cursor cursor = db.query("alarms",
                new String[]{"_id", "enable", "day_of_week", "hour", "minute"},
                "day_of_week = ?", new String[]{String.format("%d", dayOfWeek)},
                null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int enable_int = cursor.getInt(cursor.getColumnIndex("enable"));
            int day_of_week = cursor.getInt(cursor.getColumnIndex("day_of_week"));
            int hour = cursor.getInt(cursor.getColumnIndex("hour"));
            int minute = cursor.getInt(cursor.getColumnIndex("minute"));
            return new AlarmStatus(id, enable_int != 0, day_of_week, hour, minute);
        }
        return null;
    }
}
