package com.example.screen36;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity {
	AlertDialog.Builder alertDialogBuilder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
						textview.setText(hourOfDay + ":" + minute);
					}
				}, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
				true);
		// タイトルをセット
		timePickerDialog.setTitle("タイトル");
		// ダイアログを表示
		timePickerDialog.show();
	}

}
