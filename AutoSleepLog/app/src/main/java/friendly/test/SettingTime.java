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

public class SettingTime  extends Activity{
    private int convert_minute = 0;

    public int set_time() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                convert_minute = 60 * hourOfDay + minute;
            }
        };


        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute =  calendar.get(Calendar.MINUTE);

        // カレンダーインスタンスを取得
        Calendar date = Calendar.getInstance();


        // TimePickerDialogインスタンスを生成
       TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Black,
                onTimeSetListener, hourOfDay, minute, true);


        // タイトルをセット
        timePickerDialog.setTitle("時刻設定");
        // ダイアログを表示
        timePickerDialog.show();


        return convert_minute;
    }
}
