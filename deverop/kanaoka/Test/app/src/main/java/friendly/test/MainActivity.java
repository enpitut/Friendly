package friendly.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.TimePicker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;

public class MainActivity extends Activity implements OnClickListener{
    AlertDialog.Builder alertDialogBuilder;

    TimePickerDialog timePickerDialog;

    SharedPreferences sharedPref;
    SharedPreferences pref;

    TextView textview;

    //計測開始までの遡及時間
    final int pre_start_time = 10;

    //再設定ボタン
    Button confbtn;

    //設定時間・保存時間
    String str;     //  比較用

    int time_minute;    //アプリの開始の比較用
    int time_hour;      //アプリの開始の比較用

    int pre_hour;       //時間の保存用
    int pre_minute;     //分の保存用


    //preference保存時使用
    Editor e;

    //スレッド作成時使用
    MyTimerTask timerTask = null;
    Timer   mTimer   = null;
    Handler mHandler = new Handler();
    float    mLaptime = 0.0f;

    //現在時刻を格納
    String datetime;

    //計測開始のメッセージ
    TextView debug;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TextViewインスタンスを取得(結果表示用)
        ImageView img = (ImageView) findViewById(R.id.image);       //画像を取得
        setContentView(R.layout.activity_main);                     //レイアウトの取得

        findViews();
        setListeners();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
        e = sharedPref.edit();

        //読み込み
        pref = getSharedPreferences("sharedPref", MODE_PRIVATE);
        time_hour = pref.getInt("key_hour", -1);
        time_minute = pref.getInt("key_minute",-1);
        str = String.format("%02d : %02d",time_hour,time_minute);           //表示文字の準備
        textview.setText(str);                                              //表示文字のセット
// カレンダーインスタンスを取得
        Calendar date = Calendar.getInstance();
        // TimePickerDialogインスタンスを生成(ダイアログの設定)
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,       //ダイアログで設定された時の動作を記述
                                          int minute) {
                        // セットされた時刻を取得してtextviewに反映
                        textview.setText(String.format("%02d : %02d",hourOfDay,minute));
                        pre_hour=hourOfDay;
                        pre_minute=minute;
                        e.putInt("key_hour",pre_hour);
                        e.putInt("key_minute",pre_minute);
                        e.commit();
                    }
                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
                true);

        //デーモンスレッドを作成
        //1000msごとに定期実行
        if(mTimer == null){

            //タイマーの初期化処理
            timerTask = new MyTimerTask();
            mLaptime = 0.0f;
            mTimer = new Timer(true);
            mTimer.schedule( timerTask, 1000, 1000);
        }

        // タイトルをセット
        timePickerDialog.setTitle("タイトル");

        //変更後に表示
        textview = (TextView) findViewById(R.id.show_textview);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings: startActivity(new Intent(this, MyPreferences.class));
                return true;
        }
        return false;
    }
    protected void findViews(){
        // TextViewインスタンスを取得(結果表示用)
        textview = (TextView) findViewById(R.id.show_textview);
        debug = (TextView)findViewById(R.id.debug);
        confbtn = (Button)findViewById(R.id.option);
    }

    protected void setListeners() {

        confbtn.setOnClickListener(new OnClickListener() {         //confbtnが押された時の動作
            @Override
            public void onClick(View view) {
                // ダイアログを表示
                timePickerDialog.show();
            }
        });
    }
    @Override
    public void onClick(View view) {
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            // mHandlerを通じてUI Threadへ処理をキューイング
            mHandler.post( new Runnable() {
                public void run() {
                    //設定時間との比較
                    pref = getSharedPreferences("sharedPref", MODE_PRIVATE);
                    time_hour = pref.getInt("key_hour", -1);
                    time_minute = pref.getInt("key_minute",-1);

                    time_hour += 24;                            //マイナスにならない
                    time_hour -= pre_start_time;                //アラーム用の時間遡及
                    time_hour %= 24;                            //0~23

                    str = String.format("%02d : %02d",time_hour,time_minute);
                    //現在時刻を取得
                    Time time = new Time("Asia/Tokyo");
                    time.setToNow();
                    datetime = String.format("%02d : %02d",time.hour,time.minute) ;

                    //設定時間と現在時刻の比較
                    if(str.equals(datetime)){
                        debug.setText("計測開始");
                    }
                }
            });

        }
    }

}
