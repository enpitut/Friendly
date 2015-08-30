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
    TextView readTextView;

    Button writeButton;
    Button readButton;

    //再設定ボタン
    Button confbtn;

    //設定時間・保存時間
    String time;
    String str;

    //preference保存時使用
    Editor e;

    //スレッド作成時使用
    MyTimerTask timerTask = null;
    Timer   mTimer   = null;
    Handler mHandler = new Handler();
    float    mLaptime = 0.0f;

    //現在時刻を格納
    String datetime;
    TextView dateTimeView;

    //
    TextView debug;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TextViewインスタンスを取得(結果表示用)
        final TextView textview = (TextView) findViewById(R.id.textview);
        ImageView img = (ImageView) findViewById(R.id.image);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
        e = sharedPref.edit();

// カレンダーインスタンスを取得
        Calendar date = Calendar.getInstance();
        // TimePickerDialogインスタンスを生成
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // セットされた時刻を取得してtextviewに反映
                        textview.setText(String.format("%02d:%02d",hourOfDay,minute));
                        time = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                    }
                }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
                true);
        // タイトルをセット
        timePickerDialog.setTitle("タイトル");
        // ダイアログを表示
        timePickerDialog.show();
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
        textview = (TextView) findViewById(R.id.textview);

        //readTextView = (TextView)findViewById(R.id.textView1);
        //writeButton = (Button)findViewById(R.id.writeButton);
        //readButton = (Button)findViewById(R.id.readButton);

        //dateTimeView = (TextView)findViewById(R.id.dateTimeView);
        //debug = (TextView)findViewById(R.id.debug);

        confbtn = (Button)findViewById(R.id.option);

    }

    protected void setListeners() {
        writeButton.setOnClickListener(new OnClickListener(){

            //ボタンを押すことで動作
            @Override
            public void onClick(View v){

                //第一引数はキー名・第二引数は値
                e.putString("key",time);
                e.commit();
            }
        });

        //preferenceへ保存したデータの取得
        readButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getSharedPreferences("sharedPref", MODE_PRIVATE);
                str = pref.getString("key", "");

                //取得したデータの可視化
               // readTextView.setText(str);

                //デーモンスレッドを作成
                //1000msごとに定期実行
                if(mTimer == null){

                    //タイマーの初期化処理
                    timerTask = new MyTimerTask();
                    mLaptime = 0.0f;
                    mTimer = new Timer(true);
                    mTimer.schedule( timerTask, 1000, 1000);
                }
            }
        });

        confbtn.setOnClickListener(new OnClickListener() {
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
                    pref = getSharedPreferences("sharedPref",MODE_PRIVATE);
                    str = pref.getString("key", "");

                    //現在時刻を取得
                    Time time = new Time("Asia/Tokyo");
                    time.setToNow();
                    datetime = time.hour + ":" + time.minute ;

                    dateTimeView.setText(datetime);

                    //substring(x,y) : xとyの間の文字列を抜き出す
                    //設定時間と現在時刻の比較
                    if(str.equals(datetime)){
                        debug.setText("できた！！！");
                    }
                }
            });

        }
    }

}
