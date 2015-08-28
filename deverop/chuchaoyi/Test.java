 import java.util.Timer;
 
 public class Test {

    public static void main(String[] args) {
         Timer timer = new Timer();
         timer.schedule(new MyTimerTask(), 1000);
 
     }
 
 }