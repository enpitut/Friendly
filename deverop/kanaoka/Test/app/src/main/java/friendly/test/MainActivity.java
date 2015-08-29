package friendly.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;

public class MainActivity extends Activity{

    private int minute = 1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // TextViewインスタンスを取得(結果表示用)
        final TextView textview = (TextView) findViewById(R.id.textview);


       ImageView img = (ImageView)findViewById(R.id.image);
       setContentView(R.layout.activity_main);

       // SettingTime settingtime = new SettingTime();
       //  minute = settingtime.set_time();

        // セットされた時刻を取得してtextviewに反映
       // textview.setText(String.format("設定時刻は%02d:%02dです",minute/60,minute - 60*(minute/60)));

       // SavingData savingdata= new SavingData();
       // savingdata.Save(minute);

    }
    public void onStart(){
        super.onStart();

    }
    public void onRestart(){
        super.onRestart();
    }

}
