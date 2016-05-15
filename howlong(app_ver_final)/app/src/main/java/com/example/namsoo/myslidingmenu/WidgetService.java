package com.example.namsoo.myslidingmenu;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetService extends Service {

    RemoteViews remoteViews;
    NotificationManager notificationManager;
    Intent intent_temp;


    static int NOTIFICATION_ID = 111;

    @Override
    public void onCreate() {


        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intent_temp = intent;

        startWidget();
        remoteViews = new RemoteViews(getPackageName(), R.layout.widget_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String btn = null;

        try {
            btn = intent.getStringExtra("btn");
        } catch (Exception e) {
            Log.e("WidgetService", e.toString());
        }
        if (btn != null) {
            if (btn.equals("exit")) {
                notificationManager.cancelAll();
                stopService(intent);
                Intent gpsIntent = new Intent(getApplicationContext(), GPSservice.class);
                stopService(gpsIntent);
                Log.e("WidgetService", "stopService..");

                System.exit(1);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }//서비스 실행


    public void startWidget() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.widget_main);//뷰 추가
        setWidgetButtonListeners();//버튼이벤트 추가
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.smallicon)
                .setTicker("서비스 실행 중입니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews).setOngoing(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.addFlags(resultIntent.FLAG_ACTIVITY_NEW_TASK | resultIntent.FLAG_ACTIVITY_SINGLE_TOP);//화면이 없는 상태에서 만들어주는 플래그 || 화면이 있으면 화면을 재사용하세요.


        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, 0);

        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent).setAutoCancel(true);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void setWidgetButtonListeners() {

        Intent intentBtnExit = new Intent(getApplicationContext(), this.getClass());
        intentBtnExit.putExtra("btn", "exit");
        PendingIntent piExit = PendingIntent.getService(getApplicationContext(), R.id.btnExit, intentBtnExit, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btnExit, piExit);
        //이벤트 추가
    }


 /*   @Override
    public void onDestroy() {
        super.onDestroy();
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}