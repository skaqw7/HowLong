package com.example.namsoo.myslidingmenu;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

/*    boolean onetimeFlag = false;
    boolean setting_tts = true;
    boolean setting_vibration = true;*/

    Intent i;
    double curSpeed;
    boolean isEntering;
    PendingIntent pi;
    public static final double TRAFFIC_CONGESTION_VELOCITY = 40.0;

    @Override
    public void onReceive(Context context, Intent intent) {

        String TAG = "MyBroadcastReceiver";

        if (intent.getAction().equals("VDS_BroadcastReceiver")) {
            isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            curSpeed = 0; //현재 속도를 받아오는 double형 변수
            i = new Intent(context, PopUpActivity.class);
            i.putExtra("drivemode", true);


            pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (isEntering)  //true
            {
                curSpeed = GPSservice.getSpeed; //static 변수로 접근 할 것임.
                if (curSpeed <= TRAFFIC_CONGESTION_VELOCITY) {

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

        } else if (intent.getAction().equals("VMS_BroadcastReceiver")) {
            isEntering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            curSpeed = 0; //현재 속도를 받아오는 double형 변수
            i = new Intent(context, PopUpVMSActivity.class);

            pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (isEntering)  //true
            {
                curSpeed = GPSservice.getSpeed; //static 변수로 접근 할 것임.
                if (curSpeed <= TRAFFIC_CONGESTION_VELOCITY) {

                    try {
                        pi.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
//                    Log.e(TAG, "in V: " + GPSservice.getSpeed + " / T: " + GPSservice.avg_time);
                    Log.e(TAG, "VMS Log");
                }
            } else {
                Log.e(TAG, "out V: " + GPSservice.getSpeed + " / T: " + GPSservice.avg_time);
            }


        }
        //end of VDS_BroadcastReceiver part


    }
}