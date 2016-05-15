package com.example.namsoo.myslidingmenu;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class VmsBroadcastReceiver extends BroadcastReceiver {

/*    boolean onetimeFlag = false;
    boolean setting_tts = true;
    boolean setting_vibration = true;*/

    Intent i;
    double curSpeed;
    boolean isEntering;
    public static final double TRAFFIC_CONGESTION_VELOCITY = 40.0;

    @Override
    public void onReceive(Context context, Intent intent) {

        String TAG = "MyBroadcastReceiver";
        String name = intent.getAction();

        /* 진동 and TTS */
//        Intent ttsService = new Intent(context, TTS.class);
//        Vibrator v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);


        isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
        curSpeed = 0; //현재 속도를 받아오는 double형 변수
        i = new Intent(context, PopUpActivity.class);

        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (isEntering)  //true
        {
            curSpeed = GPSservice.getSpeed; //static 변수로 접근 할 것임.
            if (curSpeed <= TRAFFIC_CONGESTION_VELOCITY) {

               /* setting_tts = BaseActivity.notifications_sound;
                setting_vibration = BaseActivity.notifications_vibration;
                if (setting_tts == true && setting_vibration == true){
                    context.startService(ttsService);
                    v.vibrate(1000);
                }
                else if (setting_tts == true && setting_vibration == false) {
                    context.startService(ttsService);
                }
                else if (setting_tts == false && setting_vibration == true){
                    v.vibrate(1000);
                }*/

                try {
                    pi.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "in V: " + GPSservice.getSpeed + " / T: " + GPSservice.avg_time);
            }
        } else {
            Log.e(TAG, "out V: " + GPSservice.getSpeed + " / T: " + GPSservice.avg_time);
        }

    }
}