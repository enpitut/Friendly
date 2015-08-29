package friendly.test;

/**
 * 時刻を保存する
 */

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;

public class SavingData extends Activity{

    public SharedPreferences sharedPreferences;

    public void Save(int minute) {

       sharedPreferences = getSharedPreferences("content1",Context.MODE_PRIVATE);
       // "DataStore"という名前でインスタンスを生成
       SharedPreferences.Editor ed = sharedPreferences.edit();

       // 入力文字列を"input"に書き込むSharedPreferences.
       ed.putInt("time", minute);
       //
       ed.commit();
    }
}
