package com.toyamadaigaku.cecile.timingaccess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TIMING extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timing, menu);
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
}

    //condition:right after the sensor start counting,then try getting the sleeping time

    //get current time
import java.text.SimpleDateFormat;

public class TIMING{
    public static void main(String[] args){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


        String hehe = dateFormat.format( now );
        System.out.println(hehe);

        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        System.out.println(year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second);
    }
}

//condition:when the sensor stop counting, once again try getting the current time

//counting the sleeping time
public class timecost {
    public static void main(String[] args)
    {
        //  long startTime = System.nanoTime();             // nano sec.
        long startTime = System.currentTimeMillis();    // micro sec.

        //  Ur code

        //  long estimatedTime = System.nanoTime() - startTime;
        long estimatedTime=System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime);
    }
}