package com.example.sa__yuu_.bonnenuit;

import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.lang.String;
import java.sql.Timestamp;

import android.content.ContentValues;


public class Analyze{
    static String DBTable = "accelerations";
    static String[] DBcolumns = new String[] {"x", "y", "z", "dx", "dy", "dz"};
    static String whereSample = "timestamp";
    static String whereArgsSample = "%2015-12-05%";
    private static float[] x = {0}, y = {0}, z = {0}, dx = {0}, dy = {0}, dz = {0};
    private static float[] deltaAbsAcc = new float[40000];
    private static Vector3[] acc;
    private static float[][] fltData;
/*
    public static void sleepCheck(String where, String[] whereArgs){
        try {
            fltData = readDB(where, whereArgs);
        }catch(Exception e){
            Log.e("readError", "DBの読み込みに失敗しました");
        }
        for(int i = 0; i < 6; i++){
            switch (i){
                case 0 : insertArray(x, fltData, 0); break;
                case 1 : insertArray(y, fltData, 1); break;
                case 2 : insertArray(z, fltData, 2); break;
                case 3 : insertArray(dx, fltData, 3); break;
                case 4 : insertArray(dy, fltData, 4); break;
                case 5 : insertArray(dz, fltData, 5); break;
            }
        }


    }

    private static Timestamp getSleepStartTime(){
        acc = new Vector3[40000];
        insertVector(acc, x, y, z);
        calcDelta(acc, deltaAbsAcc);
        for(int i = 0; i < x.)
    }

    //加速度の差の絶対値が小さくなっているかを判定

    //加速度の絶対値の差が小さくなっているかを判定
    private static void deltaAbsAcc(Vector3 vct){
        vct.length();
    }

    private static void insertArray(float[] array, float[][] fltCsr, int idx){
        for(int i = 0; i < fltCsr[idx].length; i++){
            array[i] = fltCsr[idx][i];
        }
    }

    private static void insertVector(Vector3[] vct, float[] arrayx, float[] arrayy, float[] arrayz){
        for(int i = 0; i < arrayx.length; i++){
            vct[i].set(arrayx[i], arrayy[i], arrayz[i]);
        }
    }

    private static void calcDelta(Vector3[] vect, float[] deltaAbsVect){
        float[] tempAbsVect = new float[40000];
        for(int i = 0; i < x.length; i++){
            tempAbsVect[i] = vect[i].length();
        }
        for(int i = 1; i < x.length; i++){
            deltaAbsVect[i - 1] = tempAbsVect[i] - tempAbsVect[i - 1];
        }
    }
*/

    /*//DBから指定のデータをすべて二次元配列に格納し返す
    private static void readDB(String where, String[] whereArgs) throws Exception{
        float[][] fltCusor = new float[10][40000];
        Log.d(".", "readDB");
        float xVal;
        Cursor csr = MainActivity.mydb.query(DBTable, DBcolumns,
                where, whereArgs, null, null, null, null);
        if(csr.getCount() == 0) throw new Exception();
        Log.i(".", "recordCount, columnCount : " + String.valueOf(csr.getCount()) + String.valueOf(csr.getColumnCount()));
        csr.moveToFirst();
        xVal = csr.getFloat(0);
        Log.d(".", String.valueOf(xVal));
        for(int i = 0; i < csr.getCount(); i++){
            for(int j = 0; j < csr.getColumnCount(); j++){
                fltCusor[i][j] = csr.getFloat(j);
            }
            csr.moveToNext();
        }
        return fltCusor;
    }
    */

    //DBからデータの読み込みテスト
    public static void readDB(){
        Log.d("readDB", "start readDB");
        float readData;
        Cursor csr = MainActivity.mydb.query(DBTable, DBcolumns,
                "x >= ?", new String[] {"1.0"}, null, null, null, null);
        Log.d("readDB", "the number of records: " + String.valueOf(csr.getCount()));
        csr.moveToFirst();
        for(int i = 0; i < 6; i++) {
            Log.d("readDB", "first record is " + String.valueOf(csr.getFloat(0)));
        }
        csr.close();
    }

    //ダミーデータをDBに書き込む
    public static void writeDB(){
        ContentValues values = new ContentValues();
        values.put("x", 1.0f);
        values.put("y", 1.1f);
        values.put("z", 1.2f);
        values.put("dx", 2.0f);
        values.put("dy", 2.1f);
        values.put("dz", 2.2f);
        MainActivity.mydb.insert("accelerations", null, values);
        Log.d("writeDB", "insert done");
    }
}