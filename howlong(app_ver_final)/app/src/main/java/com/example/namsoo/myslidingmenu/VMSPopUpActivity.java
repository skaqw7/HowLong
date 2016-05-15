package com.example.namsoo.myslidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Vector;


public class VMSPopUpActivity extends Activity {
    public String VMSMESSAGE = "";//http://cctvsec.ktict.co.kr/2/t75IPyKGmKStbou7xPeZfzxmOB1t058vSN6PvpqsLOeP+0AMhX198chQO9xc0iHb";
    VideoView videoView;
    String TAG = "VMSPopUpActivity";

    VMSParsing vmsparsing= new VMSParsing();

    //해당 고속도로 이름 저장위한 객체선언
    Vector<String> routeName = new Vector<String>();
    //vmsID 저장위한 객체선언
    Vector<String> vmsId = new Vector<String>();
    //vmsMessage 저장위한 객체선언
    Vector<String> vmsMessage = new Vector<String>();

    TextView text_routeName = (TextView)findViewById(R.id.text_routeName);
    TextView text_vmsMessage = (TextView)findViewById(R.id.text_vmsMessage);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_vms);





        vmsparsing.execute(null, null, null);

        while(true){
            try{
                Thread.sleep(1000); //1초마다 실행


                if(vmsparsing.flag==true){

                    routeName = vmsparsing.routeName;
                    vmsId = vmsparsing.vmsId;
                    vmsMessage = vmsparsing.vmsMessage;


                    break;//반복문 종료
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        text_routeName.setText(routeName.toString());
        text_vmsMessage.setText(vmsMessage.toString());



//        MyPreferencesActivity.gogoFunc("b"); //다시 false로..
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //투명배경

        Button btn_finish = (Button) findViewById(R.id.button);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MyPreferencesActivity.gogoFunc("a"); //메인을 죽이기 위해 flag를 true로
                finish();
                Log.e(TAG, "onClick()");


            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServiceMethod(this);
        Log.e(TAG, "onDestroy()");

    }

    public void stopServiceMethod(Context context) {
        Intent Service = new Intent(context, TTS.class);
        context.stopService(Service);
        Log.e(TAG, "stopServiceMethod()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_up, menu);
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
