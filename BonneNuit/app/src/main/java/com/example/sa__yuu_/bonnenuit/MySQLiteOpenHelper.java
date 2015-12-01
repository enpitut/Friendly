package com.example.sa__yuu_.bonnenuit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MySQLiteOpenHelper extends SQLiteOpenHelper {
    static final String DB = "bonneuit.db";
    static final int DB_VERSION = 7;

    static final String CREATE_ALRMS_TABLE = "CREATE TABLE alarms ( _id integer primary key autoincrement, enable boolean not null, hour integer not null, minute integer not null );";
    static final String DROP_ALRMS_TABLE = "DROP TABLE IF EXISTS alarms;";

    static final String CREATE_ACCELERATIONS_TABLE = "CREATE TABLE accelerations ( _id integer primary key autoincrement, x float not null, y float not null, z float not null, dx float not null, dy float not null, dz float not null, timestamp TIMESTAMP DEFAULT (DATETIME('now','localtime')) );";
    static final String DROP_ACCELERATIONS_TABLE = "DROP TABLE IF EXISTS accelerations;";

    public MySQLiteOpenHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALRMS_TABLE);
        db.execSQL(CREATE_ACCELERATIONS_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_ALRMS_TABLE);
        db.execSQL(DROP_ACCELERATIONS_TABLE);
        onCreate(db);
    }
}