package friendly.test;

/**
 * 時刻を保存する
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SavingData extends Activity {

    public void Save(int minute) {
        // "DataStore"という名前でインスタンスを生成
        SharedPreferences dataStore = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = dataStore.edit();
        // 入力文字列を"input"に書き込むSharedPreferences.
        ed.putInt("time", minute);
        ed.commit();
    }
}
