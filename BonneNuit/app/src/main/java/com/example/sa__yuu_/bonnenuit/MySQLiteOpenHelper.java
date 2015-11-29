package com.example.sa__yuu_.bonnenuit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MySQLiteOpenHelper extends SQLiteOpenHelper {
    static final String DB = "bonneuit.db";
    static final int DB_VERSION = 2;
    static final String CREATE_TABLE = "create table alarms ( _id integer primary key autoincrement, enable boolean not null, hour integer not null, minute integer not null );";
    static final String DROP_TABLE = "drop table alarms;";

    public MySQLiteOpenHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}