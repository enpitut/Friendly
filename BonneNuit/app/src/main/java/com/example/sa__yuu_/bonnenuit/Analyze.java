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
    private static float[] x = new float[40000], y = new float[40000], z = new float[40000], dx = new float[40000], dy = new float[40000], dz = new float[40000];
    private static float[] deltaAbsAcc = new float[40000];
    private static Vector3[] acc = new Vector3[40000], dltAcc = new Vector3[40000];
    private static float[][] fltData;

    public static void sleepCheck(String where, String[] whereArgs){
        float[] tmpvct = x;
        int tmpidx = 0;
        int time = -1;
        try {
            fltData = readDB(where, whereArgs);
        }catch(Exception e){
            Log.e("readError", "DBの読み込みに失敗しました");
        }
        for(int i = 0; i < 6; i++){
            switch (i){
                case 0 : tmpvct = x; tmpidx = 0; break;
                case 1 : tmpvct = y; tmpidx = 1; break;
                case 2 : tmpvct = z; tmpidx = 2; break;
                case 3 : tmpvct = dx; tmpidx = 3; break;
                case 4 : tmpvct = dy; tmpidx = 4; break;
                case 5 : tmpvct = dz; tmpidx = 5; break;
            }
            insertArray(tmpvct, fltData, tmpidx);
        }
        insertVector(acc, x, y, z);
        insertVector(dltAcc, dx, dy, dz);
        time = smallerAbsDltAcc(dltAcc);
        Log.d("sleepCheck", "time1 " + time);
        time = smallerDeltaAbsAcc(acc);
        Log.d("sleepCheck", "time2 " + time);
    }

/*
    private static Timestamp getSleepStartTime(){
        acc = new Vector3[40000];
        insertVector(acc, x, y, z);
        calcDelta(acc, deltaAbsAcc);
        for(int i = 0; i < x.)
    }
*/

    //加速度の差の絶対値が小さくなっているかを判定
    //timestamp型を返したい
    //dx, dy, dzのVector3を引数
    private static int smallerAbsDltAcc(Vector3[] vctArray){
        float[] absVct = new float[40000];
        int cnt = 0;
        for(int i = 0; i < vctArray.length; i++){
            try{
                absVct[i] = vctArray[i].length();
            }catch(NullPointerException e){
                break;
            }
        }
        for(int i = 1; i < absVct.length; i++){
            try{
                if(absVct[i] < absVct[i - 1]){
                    if(++cnt > 300) return i - 300; //連続でっていう条件がまだ入ってない(TODO)
                }
            }catch(NullPointerException e){
                break;
            }
        }
        return 0;
    }

    //加速度の絶対値の差が小さくなっているかを判定
    //timestamp型をreturnしたい
    //x, y, zのVector3を引数
    private static int smallerDeltaAbsAcc(Vector3[] vctArray){
        float[] deltaVctLeng;
        int cnt = 0;
        deltaVctLeng = calcDelta(vctArray);
        for(int i = 0; i < deltaVctLeng.length; i++){
            try{
                if(deltaVctLeng[i] < 0){
                    if(++cnt > 300) return i - 300;   //連続でっていう条件がまだ入ってない(TODO)
                }
            }catch(NullPointerException e){
                break;
            }
        }
        return 0;
    }

    private static void insertArray(float[] array, float[][] fltCsr, int idx){
        for(int i = 0; i < fltCsr[idx].length; i++){
            try{
                array[i] = fltCsr[idx][i];
            }catch(NullPointerException e){
                break;
            }
        }
    }

    private static void insertVector(Vector3[] vct, float[] arrayx, float[] arrayy, float[] arrayz){
            for(int i = 0; i < arrayx.length; i++){
                try{
                    vct[i].set(arrayx[i], arrayy[i], arrayz[i]);
                }catch(NullPointerException e){
                    break;
                }
            }
    }

    private static float[] calcDelta(Vector3[] vect){
        float[] tempAbsVect = new float[40000];
        float[] deltaAbsVct = new float[40000];
        for(int i = 0; i < x.length; i++){
            try{
                tempAbsVect[i] = vect[i].length();
            }catch(NullPointerException e){
                break;
            }
        }
        for(int i = 1; i < x.length; i++){
            try{
                deltaAbsVct[i - 1] = tempAbsVect[i] - tempAbsVect[i - 1];
            }catch(NullPointerException e){
                break;
            }
        }
        return deltaAbsVct;
    }


    //DBから指定のデータをすべて二次元配列に格納し返す
    //パラメータ1で条件パラメータ2で ? の部分の具体的な値をStringの配列で渡す
    //タイムスタンプの範囲を指定したい。スタート位置はアラームの設定から取ってこられる？終了位置も起床時間を記録してるなら取ってこられる
    protected static float[][] readDB(String where, String[] whereArgs){
        float[][] fltCusor = new float[10][40000];
        Log.d("readDB", "readDB");
        float xVal;
        Cursor csr = MainActivity.mydb.query(DBTable, DBcolumns,
                where, whereArgs, null, null, null, null);
        if(csr.getCount() == 0) Log.e("readDB", "----------the number of records is 0----------");
        Log.i("readDB", "recordCount, columnCount : " + String.valueOf(csr.getCount()) + " " + String.valueOf(csr.getColumnCount()));
        csr.moveToFirst();
        xVal = csr.getFloat(0);
        Log.d("readDB", String.valueOf(xVal));
        for(int i = 0; i < csr.getCount(); i++){
            for(int j = 0; j < csr.getColumnCount(); j++){
                try{
                    Log.d("readDB", "i: " + i + " j: " + j);
                    fltCusor[j][i] = csr.getFloat(j);
                }catch(NullPointerException e){
                    break;
                }
            }
            csr.moveToNext();
        }
        return fltCusor;
    }

/*
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
*/

    //ダミーデータをDBに書き込む
    public static void writeDB() {
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