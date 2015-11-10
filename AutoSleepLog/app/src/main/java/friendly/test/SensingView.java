package friendly.test;

/**
 * Created by TSURUMI on 15/11/10.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SensingView extends Activity{
    //起床ボタン
    Button wake_up_btn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensing);

        wake_up_btn = (Button)findViewById(R.id.wakeup);

        wake_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TOP画面へ遷移 応急処置？最適解なのかわからない
                finish();
            }
        });
    }
}
