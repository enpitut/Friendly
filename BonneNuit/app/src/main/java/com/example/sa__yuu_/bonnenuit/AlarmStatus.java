package com.example.sa__yuu_.bonnenuit;
import java.io.Serializable;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void toggle() { enable = !enable; }

    public String getAlarmTime() {
        return String.format("%02d:%02d", mHour, mMinute);
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
}
