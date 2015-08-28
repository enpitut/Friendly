package friendly.test;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity {

    private EditText editText;
    private TextView textWrite, textRead;

    private SharedPreferences dataStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // "DataStore"という名前でインスタンスを生成
        SharedPreferences dataStore = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = dataStore.edit();

        // TextViewインスタンスを取得(結果表示用)
        final TextView textview = (TextView) findViewById(R.id.textview);
        // カレンダーインスタンスを取得
        Calendar date = Calendar.getInstance();
        // TimePickerDialogインスタンスを生成
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // セットされた時刻を取得してtextviewに反映
                        textview.setText("設定時刻は " + hourOfDay + ":" + minute + " です");

                        // 入力文字列を"input"に書き込むSharedPreferences.
                        ed.putInt("input", hourOfDay * 60 + minute);
                        ed.commit();
                    }
                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
                true);

        // タイトルをセット
        timePickerDialog.setTitle("時刻設定");
        // ダイアログを表示
        timePickerDialog.show();
    }

}
