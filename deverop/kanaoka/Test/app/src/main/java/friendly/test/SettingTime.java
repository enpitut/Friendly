package friendly.test;

/**
 * 時刻設定
 */

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class SettingTime extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViewインスタンスを取得(結果表示用)
        final TextView textview = (TextView) findViewById(R.id.textview);
        // カレンダーインスタンスを取得
        Calendar date = Calendar.getInstance();
        // TimePickerDialogインスタンスを生成
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // セットされた時刻を取得してtextviewに反映
                        textview.setText(String.format("設定時刻は%02d:%02dです", hourOfDay, minute));
                        SavingData.Save(hourOfDay, minute);
                    }
                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true);

        // タイトルをセット
        timePickerDialog.setTitle("時刻設定");
        // ダイアログを表示
        timePickerDialog.show();
    }


}
