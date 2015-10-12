package cecile0207tz.startcounting;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/8/28.
 */
//timer part
public class start {
    public static void main(String[] args) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // task to run goes here
                System.out.println("start !!!");
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 1 * 1000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    } // end of main
}
