import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
 
    //condition:right after the sensor start counting,then try getting the sleeping time

    //get current time


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

//condition:when the sensor stop counting, once again try getting the current time as the getting up time

//counting the whole sleeping time
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