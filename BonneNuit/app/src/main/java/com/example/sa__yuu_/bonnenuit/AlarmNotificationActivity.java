package com.example.sa__yuu_.bonnenuit;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmNotificationActivity extends Activity {

    private Button setButton;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_nortification);

        setButton = (Button) findViewById(R.id.stop_button);
        setButton.setOnClickListener(onStopButtonClick);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        //        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        //        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
        //        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 音を鳴らす
        mp = MediaPlayer.create(this, R.raw.star);
        mp.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_nortification, menu);
        return true;
    }

    private View.OnClickListener onStopButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mp.stop();
        }

    };

}
