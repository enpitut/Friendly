package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class AlarmSettingActivity extends Activity {

    private Button deleteButton;
    private TimePicker timePicker;
    private AlarmStatus status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        status = (AlarmStatus)getIntent().getSerializableExtra("clickedStatus");
        deleteButton = (Button)findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(onDeleteButtonClick);
        timePicker = (TimePicker)findViewById(R.id.time_picker);
        //timePicker.setHour(status.calendar.get(Calendar.HOUR_OF_DAY));
        //timePicker.setMinute(status.calendar.get(Calendar.MINUTE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onDeleteButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName("com.example.sa__yuu_.bonnenuit", "com.example.sa__yuu_.bonnenuit.AlarmActivity");
            startActivity(intent);
        }

    };
}
