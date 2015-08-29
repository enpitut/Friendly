package friendly.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity{

    private int minute;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SettingTime settingtime = new SettingTime();
        minute = settingtime.set_time();
        SavingData savingdata= new SavingData();
        savingdata.Save(minute);
    }
    public void onStart(){
        super.onStart();

    }
    public void onRestart(){
        super.onRestart();
    }

}
