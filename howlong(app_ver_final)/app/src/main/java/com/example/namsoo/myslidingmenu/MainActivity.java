package com.example.namsoo.myslidingmenu;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.view.MenuItem;

import java.util.List;


public class MainActivity extends BaseActivity {

    String TAG = "MainActivity";
    boolean backPressedFlag = true;
    static ActionBar actionBar;
    private NotificationManager notificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#dd6f4d")));
        fragmentReplace(1);//기본 화면

    }

    public boolean serviceChk() {
        boolean serviceChkflag = false;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(150);
        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            if (rsi.service.getClassName().equals(
                    "com.example.namsoo.myslidingmenu.GPSservice")) {
                // 해당 서비스가 돌고 있으면
                Log.e(TAG, "com.example.namsoo.myslidingmenu.GPSservice FOUND");
                serviceChkflag = true;
                break;
            } else {// Log.d("ROSA", "Class Name : " + rsi.service.getClassName());
                serviceChkflag = false;
            }
        }
        return serviceChkflag;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                toggle();
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "onBackPressed()");
        //  userMapFragment.stopMyLocation();
  /*      backPressedFlag = serviceChk();
        if(backPressedFlag == false)
        {
            backPressCloseHandler.onBackPressed();
        }*/

    }

    @Override
    protected void onUserLeaveHint() {
        Log.d(TAG, "Home Button Touch");
        super.onUserLeaveHint();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
        backPressedFlag = serviceChk();
        if (backPressedFlag == true) { //서비스가 있을때만 올라감
//            Toast.makeText(getApplicationContext(), "BackGround!", Toast.LENGTH_LONG).show();
            Intent widgetIntent = new Intent(this, WidgetService.class);
            startService(widgetIntent);
        }

    }
}
