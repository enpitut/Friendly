// Analyze.java
// 就寝判定を行うためのプログラム

/*  --各内部メソッド概要--

public:
boolean sleepCheck(String where, String[] whereArgs);                                               //就寝判定を行なう
void writeDB(String key,String table,String arg);                                           //Stringのでーた一つ(就寝時間)をDBに書き込む
static void writeDummyData()                                                                //ダミーデータをDBに書き込む(おそらく不要)
static void awake();                                                                            //睡眠中の判定を解除し、sleepcheckの起動を調整できるようにする
static void sleep();                                                                            //睡眠中の判定にしてsleepcheckの起動を調整できるようにする
static boolean getsleeping();                                                               //睡眠中かどうかの値を得る


private:
static boolean smallerAbsDltAcc(Vector3[] vctArray);                                            //就寝判定の条件式その1:加速度の差の絶対値が閾値以下かを判定,dx, dy, dzのVector3を引数
static boolean smallerDeltaAbsAcc(Vector3[] vctArray);                                          //就寝判定の条件式その2:加速度の絶対値の差が閾値以下かを判定,x, y, zのVector3を引数
static void insertArray(float[] array, float[][] fltCsr, int idx)                           //データを1次元配列にコピー             (内部処理用)
static void insertVector(Vector3[] vct, float[] arrayx, float[] arrayy, float[] arrayz);    //ベクタオブジェクトのインスタンスを生成(内部処理用)
static float[] calcDelta(Vector3[] vect);                                                   //加速度ベクタの絶対値の差分を計算      (内部処理用)

protected:

//パラメータ1で条件パラメータ2で ? の部分の具体的な値をStringの配列で渡す
//タイムスタンプの範囲を指定したい。スタート位置はアラームの設定から取ってこられる？終了位置も起床時間を記録してるなら取ってこられる
static float[][] readDB(String where, String[] whereArgs)                                   //引数1,2によりDBから検索した変位、加速度を取得:DBから指定のデータをすべて二次元配列に格納し返す

*/
package com.example.sa__yuu_.bonnenuit;

import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.lang.String;
//import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;


public class Analyze{
    static String DBTable = "accelerations";
    static String[] DBcolumns = new String[] {"x", "y", "z"};
    static String[] DB_timestamp = new String[]{"timestamp"};
    static String whereSample = "timestamp";
    static String whereArgsSample = "%2015-12-05%";
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Calendar calendar2;
    static String startTime;
    static String showsleeping = "awake";
    static int cnt = 0, cnt2 = 0;

    protected static String sleeptime = "empty";
    private static float[] x = new float[40000], y = new float[40000], z = new float[40000], dx = new float[40000], dy = new float[40000], dz = new float[40000];
    private static float[] deltaAbsAcc = new float[40000];
    private static Vector3[] acc = new Vector3[40000], dltAcc = new Vector3[40000];
    private static float[][] fltData;
    private static boolean sleeping;
    private static Cursor csracc;

    // SleepCheck:就寝判定を行なって、タイムスタンプを返す
    public static boolean sleepCheck(String where, String[] whereArgs){
        float[] tmpvct = x;
        int tmpidx = 0;
        int time = -1;

        try {
            fltData = readDB(where, whereArgs);
        }catch(Exception e){
            Log.e("readError", "DBの読み込みに失敗しました");
        }
        if(fltData != null) {

            for (int i = 0; i < 6; i++) {                         //処理しやすいようにfltDataからx,y,z...の配列に流し込み
                switch (i) {
                    case 0:
                        tmpvct = x;
                        tmpidx = 0;
                        break;
                    case 1:
                        tmpvct = y;
                        tmpidx = 1;
                        break;
                    case 2:
                        tmpvct = z;
                        tmpidx = 2;
                        break;
                }
                insertArray(tmpvct, fltData, tmpidx);
            }

            insertVector(acc, x, y, z);                         //前に処理したものについてacc,dltaccに流し込み、以降それぞれvecとして扱える
            //insertVector(dltAcc, dx, dy, dz);
            dltAcc = calcDeltaVector(acc);                      //DBにdx,dy,dzを用意しなくてもdltaccをここで計算

            if (smallerAbsDltAcc(dltAcc) || smallerDeltaAbsAcc(acc)) {
                return true;
            }
        }
        return false;
    }

    protected static String sleepCheck2(String where, String[] whereArgs){
        Log.d("sleepCheckLight", "start");
        if(getsleeping()){
            Log.i("sleepcheck2", "already sleeping");
            return sleeptime;
        }
        else{
            preparedReadDB(where, whereArgs);
            for(int i = 0; ; i++){
                if(readDBL(i + 1) == Vector3.ZERO){
                    Log.e("sleepcheck2", "DB is end");
                    return "unknown";
                }
                else {
                    if (smallerAbsDltAcc(calcDeltaVector(new Vector3[]{readDBL(i), readDBL(i + 1)}))
                            && smallerDeltaAbsAcc(new Vector3[]{readDBL(i)})) {
                        //sleeptime = commitSleepTime();
                        //sleep(sleeptime);
                        //return sleeptime;
                        if(cnt2++ >= 300) return startTime;
                    }
                }
            }
        }
    }

    //睡眠中の判定を解除
    public static void awake(){
        sleeping = false;
        showsleeping = "awake!!";
    }
    //睡眠中の判定に変更

    public static void sleep(){
        sleeping = true;
        showsleeping = "sleeping...";
    }

    public static void sleep(String strTimestamp){
        sleeping = true;
        showsleeping = strTimestamp;
    }

    public static boolean getsleeping(){
        return sleeping;
    }

    private static String commitSleepTime(){
        Date date = new Date();
        String currentTime = sdf.format(date);
        writeDB("sleeptime", "sleeptime", currentTime);
        Log.i("commitSleepTime", currentTime);
        return currentTime;
    }

    //加速度の差の絶対値が閾値以下かを判定
    //dx, dy, dzのVector3を引数
    private static boolean smallerAbsDltAcc(Vector3[] vctArray){
        float[] absVct = new float[40000];
        int cnt = 0;
        for(int i = 0; i < vctArray.length; i++){
            try{
                absVct[i] = vctArray[i].length();
            }catch(NullPointerException e){
                break;
            }
        }
        for(int i = 0; i < absVct.length; i++){
            if(absVct[i] > 5.0){
                    return false;
            }
        }
        return true;
    }

    //加速度の絶対値の差が閾値以下かを判定
    //x, y, zのVector3を引数
    private static boolean smallerDeltaAbsAcc(Vector3[] vctArray){
        float[] deltaVctLeng;
        int cnt = 0;
        deltaVctLeng = calcDelta(vctArray);
        for(int i = 0; i < deltaVctLeng.length; i++){
            try{
                if(deltaVctLeng[i] > 0.0){
                     return false;
                }
            }catch(NullPointerException e){
                break;
            }
        }
        return true;
    }


    //データを1次元配列にコピー
    private static void insertArray(float[] array, float[][] fltCsr, int idx){
        for(int i = 0; i < fltCsr[idx].length; i++){
            try{
                array[i] = fltCsr[idx][i];
            }catch(NullPointerException e){
                break;
            }
        }
    }

    //ベクタオブジェクトのインスタンスを生成
    private static void insertVector(Vector3[] vct, float[] arrayx, float[] arrayy, float[] arrayz){
            for(int i = 0; i < arrayx.length; i++){
                try{
                    vct[i].set(arrayx[i], arrayy[i], arrayz[i]);
                }catch(NullPointerException e){
                    break;
                }
            }
    }
    protected static Vector3[] calcDeltaVector(Vector3[] vct){
        Vector3[] deltaVector = new Vector3[vct.length];
        for(int i = 1; i < vct.length; i++){
            vct[i].subtract(vct[i - 1]);
            //deltaVector[i - 1].set(vct[i]);
            deltaVector[i - 1] = new Vector3(vct[i]);
        }
        return deltaVector;
    }

    //加速度ベクタの絶対値の差分を計算
    private static float[] calcDelta(Vector3[] vect){
        float[] tempAbsVect = new float[40000];
        float[] deltaAbsVct = new float[40000];
        for(int i = 0; i < vect.length; i++){
            try{
                tempAbsVect[i] = vect[i].length();
            }catch(NullPointerException e){
                break;
            }
        }
        for(int i = 1; i < vect.length; i++){
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
        float xVal = 0;
        Cursor csr = MainActivity.mydb.query(DBTable, DBcolumns,
                where, whereArgs, null, null, null, null);
        if(csr.getCount() == 0) Log.e("readDB", "----------the number of records is 0----------");
        Log.i("readDB", "recordCount, columnCount : " + String.valueOf(csr.getCount()) + " " + String.valueOf(csr.getColumnCount()));
        csr.moveToFirst();
        //xVal = csr.getFloat(0);
        Log.d("readDB", String.valueOf(xVal));
        Log.d("readDB", "Getcount:"+String.valueOf(csr.getCount()));


        //for(int i = 0; i < csr.getCount(); i++){
        for(int i = 0; i < 50; i++){

                for(int j = 0; j < csr.getColumnCount(); j++){
                    try{
                        fltCusor[j][i] = csr.getFloat(j);
                    }catch(NullPointerException e){
                        Log.d("error","ぬるぽ");
                        break;
                    }catch(ArrayIndexOutOfBoundsException e){
                        break;
                    }
                }
            csr.moveToNext();
        }
        return fltCusor;
    }

    private static boolean preparedReadDB(String where, String[] whereArgs){
        Log.d("preparedReadDB", "start");
        csracc = MainActivity.mydb.query(DBTable, DBcolumns, where, whereArgs, null, null, null, null);
        if(csracc.getCount() == 0) {
            Log.e("preparedReadDB", "the number of record is 0");
            return false;
        }
        else {
            Log.i("preparedReadDb", "recordCount, columnCount : " + String.valueOf(csracc.getCount()) + " " + String.valueOf(csracc.getColumnCount()));
            csracc.moveToFirst();
            return true;
        }
    }

    protected static Vector3 readDBL(int idx){
        Log.d("readDBLine", "start. index: " + idx);
        Vector3 vctr = new Vector3();
        if(!csracc.moveToPosition(idx)) {
            Log.e("readDBL", "out of index");
            return Vector3.ZERO;
        }
        else {
            try {
                vctr.set(csracc.getFloat(0), csracc.getFloat(1), csracc.getFloat(2));
            } catch (CursorIndexOutOfBoundsException e) {
                Log.e("readDBL", "CusorIndexOutOfBoundsException");
            }
            return vctr;
        }
    }

    //String一つをDBに書き込む(一応寝た時間を想定:writeDBS("sleeptime","sleeptime",String timestamp))
    public static void writeDB(String key,String table,String arg){
        ContentValues values = new ContentValues();
        values.put(key, arg);
        MainActivity.mydb.insert(table, null, values);
        Log.d("writeDB", "insert done");
    }

    /*
    //ダミーデータをDBに書き込む、不要なら後で削除します
    public static void writeDummyData() {
        ContentValues values = new ContentValues();
        values.put("x", 1.0f);
        values.put("y", 1.1f);
        values.put("z", 1.2f);
        values.put("dx", 2.0f);
        values.put("dy", 2.1f);
        values.put("dz", 2.2f);
        MainActivity.mydb.insert("accelerations", null, values);
        Log.d("writeDB", "DummyData insert done");
    }
    */
}