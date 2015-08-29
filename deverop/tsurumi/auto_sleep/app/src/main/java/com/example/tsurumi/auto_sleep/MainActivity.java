package com.example.tsurumi.auto_sleep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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


public class MainActivity extends Activity {
    AlertDialog.Builder alertDialogBuilder;

    SharedPreferences sharedPref;
    SharedPreferences pref;

    TextView textview;
    TextView readTextView;
    //EditText writeEditText;

    Button writeButton;
    Button readButton;

    String time;
    String str;

    Editor e;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
        e = sharedPref.edit();

        // カレンダーインスタンスを取得
        Calendar date = Calendar.getInstance();
        // TimePickerDialogインスタンスを生成
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,int hourOfDay,
                                          int minute) {
                        // セットされた時刻を取得してtextviewに反映
                        textview.setText(hourOfDay + ":" + minute);
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

        readTextView = (TextView)findViewById(R.id.textView1);
        //writeEditText = (EditText)findViewById(R.id.editText1);
        writeButton = (Button)findViewById(R.id.writeButton);
        readButton = (Button)findViewById(R.id.readButton);

    }

    protected void setListeners() {
        writeButton.setOnClickListener(new OnClickListener(){

            //ボタンを押すことで動作
            @Override
            public void onClick(View v){

                //第一引数はキー名・第二引数は値
                e.putString("key",time);
                e.commit();


            //外部ファイルからのデータ取得
//                try{
//                    File file = new File("c:¥¥tmp¥¥test.txt");
//                    FileReader filereader = new FileReader(file);
//
//                    int ch = filereader.read();
//                    while(ch != -1){
//                        System.out.print((char)ch);
//
//                        ch = filereader.read();
//                    }
//                }catch(FileNotFoundException e){
//                    System.out.println(e);
//                }catch(IOException e){
//                    System.out.println(e);
//                }
            }


        });

        //preferenceへ保存したデータの取得
        readButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getSharedPreferences("sharedPref",MODE_PRIVATE);
                str = pref.getString("key", "");

                //取得したデータの可視化
                readTextView.setText(str);
            }
        });
    }


    //ファイル出力による保存
//    protected void saveText(){
//        String message = "";
//        String fileName = textview.getText().toString();
//        String inputText = readTextView.getText().toString();
//        try {
//            FileOutputStream outStream = openFileOutput(fileName, MODE_PRIVATE);
//            OutputStreamWriter writer = new OutputStreamWriter(outStream);
//            writer.write(inputText);
//            writer.flush();
//            writer.close();
//            message = "File saved.";
//        } catch (FileNotFoundException e) {
//            message = e.getMessage();
//            e.printStackTrace();
//        } catch (IOException e) {
//            message = e.getMessage();
//            e.printStackTrace();
//        }
//
//        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
//    }
}
