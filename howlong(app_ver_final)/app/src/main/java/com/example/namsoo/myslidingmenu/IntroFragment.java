package com.example.namsoo.myslidingmenu;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;

import java.util.List;

/**
 * Created by SangWook on 2015-08-03.
 */
public class IntroFragment extends Fragment {
    String TAG = "IntroFragment";
    CheckBox checkBox;
    boolean isPressed = false;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean autoFlag = false;
    boolean flag = false;

    List<ActivityManager.RunningServiceInfo> rs;
    ActivityManager am;




    LocationManager locationManager;
    boolean isGps = false;
    private Dialog dialog;
    SnackBar snackbar;

    static boolean forTest1 = false;
    static boolean forTest2 = false;

    //dduiddui
    SoundPool sound;
    int soundId;

    public static IntroFragment newInstance(boolean flag) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putBoolean("onoff", flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_intro, container, false);


        //final Intent Service1 = new Intent(getActivity(), GPSservice.class);

        isPressed = getArguments() != null ? getArguments().getBoolean("onoff") : false;
        prefs = getActivity().getSharedPreferences("setting", 0);
        autoFlag = prefs.getBoolean("flag", false);
//        autoFlag = false;
//        Toast.makeText(getActivity(), "autoFlag : " + autoFlag, Toast.LENGTH_LONG).show();
//        ((MainActivity) getActivity()).chkFlag = autoFlag;
        Log.e(TAG, "onCreateView()");
        checkBox = (CheckBox) rootView.findViewById(R.id.check_box);
//        Typeface face=Typeface.createFromAsset(getActivity().getAssets(), "fonts/폰트이름.ttf");


        //dialog.setOnAcceptButtonClickListener(View.OnClickListener onAcceptButtonClickListener);
        //dialog.setOnCancelButtonClickListener(View.OnClickListener onCancelButtonClickListener);
                   /*     ButtonFlat acceptButton = dialog.getButtonAccept();
                        ButtonFlat cancelButton = dialog.getButtonCancel();*/
        sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = sound.load(getActivity(), R.raw.dduiddui, 1);


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getActivity().getSystemService(
                        Context.LOCATION_SERVICE);
                isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //GPS 사용 가능 여부
                if (checkBox.isChecked() == true) {
                    sound.play(soundId, 1f, 1f, 0, 0, 1f);//뛰뛰


                    if (isGps == true) {
                        autoFlag = true;
                        getActivity().startService(new Intent(getActivity(), GPSservice.class));

                        snackbar = new SnackBar(getActivity(), "모의주행을\n시작하겠습니까?", "시작하기", testonAcceptButtonClickListener);

                        // snackbar.setColorButton(Color.YELLOW);

                        snackbar.setMessageTextSize(17.0f);
                        snackbar.setDismissTimer(4000);
                        snackbar.show();
//                        Toast.makeText(getActivity(), "서비스가 시작되었습니다.", Toast.LENGTH_LONG).show();
                    } else if (isGps == false) {


                        Dialog dialog = new Dialog(getActivity(), "위치정보 이용 동의", "HowLong의 위치기반 서비스를 이용하려면 현재 위치정보 사용에 대한 동의가 필요합니다.");
                        dialog.addCancelButton("cancel");
                        // Set accept click listenner
                        dialog.setOnAcceptButtonClickListener(onAcceptButtonClickListener);

                        // Set cancel click listenner
                        dialog.setOnCancelButtonClickListener(onCancelButtonClickListener);
                        // Acces to accept button
                        ButtonFlat acceptButton = dialog.getButtonAccept();

                        dialog.setButtonAccept(acceptButton);
                        // Acces to cancel button
                        ButtonFlat cancelButton = dialog.getButtonCancel();

                        dialog.show();

                        autoFlag = false;
                        checkBox.setChecked(false);
                    }

                    Log.e(TAG, "off > on");
                } else if (checkBox.isChecked() == false) {
                    autoFlag = false;
                    getActivity().stopService(new Intent(getActivity(), GPSservice.class));
                    getActivity().stopService(new Intent(getActivity(), PopUpActivity.class));
//                    SnackBar snackbar = new SnackBar(getActivity(), "HowLong 서비스가 중지되었습니다.");
//                    snackbar.show();
                    Toast.makeText(getActivity(), "서비스가 중지되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "on > ff");
                }
            }
        });


        return rootView;
    }

    private View.OnClickListener testonAcceptButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        /*    ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.CustomDialog);
            progressDialog.setTitle("제발");
            progressDialog.setMessage("...");
            progressDialog.show();*/
            BaseActivity.sm.setSlidingEnabled(false);
            MainActivity.actionBar.setHomeButtonEnabled(false);


            ProgressBarCircularIndeterminate bar = (ProgressBarCircularIndeterminate) getActivity().findViewById(R.id.progressBarCircularIndeterminate);
            bar.setVisibility(View.VISIBLE);
            checkBox.setEnabled(false);

            Log.e(TAG, "1 Toast");


//            Toast.makeText(getActivity(), "화면을 터치하지 마세요~ ", Toast.LENGTH_LONG).show();


            Toast.makeText(getActivity(), "모의주행을 시작합니다.", Toast.LENGTH_SHORT).show();


            Log.e(TAG, "2 Toast");

            Toast.makeText(getActivity(), "서울 외곽순환고속도로에 진입합니다. ", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "3 Toast");

            Toast.makeText(getActivity(), "도로전광판 알림을 시작합니다.", Toast.LENGTH_SHORT).show();


            //Intent intent = new Intent (getActivity(), PopUpActivity.class);
            // startActivity(intent);


            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                //Do Something
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ProgressBarCircularIndeterminate bar = (ProgressBarCircularIndeterminate) getActivity().findViewById(R.id.progressBarCircularIndeterminate);
                    bar.setVisibility(View.GONE);

                    Intent intent = new Intent(getActivity(), PopUpVMSActivity.class);
                    intent.putExtra("drivetestmode", true);

                    startActivity(intent);
                    finish();
                }
            }, 6000); // 1000ms


//            Handler mHandler = new Handler();
//            mHandler.postDelayed(new Runnable() {
//                //Do Something
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    Intent intent = new Intent(getActivity(), PopUpVMSActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 1000); // 1000ms

        }


    };

    private void finish() {
        //Intent intent = new Intent (getActivity(), PopUpVMSActivity.class);
        // startActivity(intent);
//
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            //Do Something
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(getActivity(), PopUpActivity.class);
//                startActivity(intent);
//                finish2();
//            }
//        }, 16000); // 1000ms
//

        //Toast.makeText(getActivity(), "모의테스트를 종료합니다", Toast.LENGTH_LONG).show();


    }



    private View.OnClickListener onAcceptButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };
    private View.OnClickListener onCancelButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
//        autoFlag = prefs.getBoolean("flag", false);
        boolean flag2 = serviceCheck();
        if (flag2 == true) { //서비스가 있으면
            checkBox.setChecked(true); //그냥 그대로 on 상태
            Log.e(TAG, "Service ON");
        } else {
            checkBox.setChecked(false); //서비스가 없으면 off로 변환
            Log.e(TAG, "Service OFF");

        }

        if (forTest1) {
            ProgressBarCircularIndeterminate bar = (ProgressBarCircularIndeterminate) getActivity().findViewById(R.id.progressBarCircularIndeterminate);

//            if (SplashScreen.failLoadFlag == true) {
            if (true) {

                bar.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "정체구간에 진입합니다.", Toast.LENGTH_SHORT).show();
                Handler mHandler = new Handler();


                mHandler.postDelayed(new Runnable() {
                    //Do Something
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        ProgressBarCircularIndeterminate bar = (ProgressBarCircularIndeterminate) getActivity().findViewById(R.id.progressBarCircularIndeterminate);
                        bar.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), PopUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000); // 1000ms

                forTest1 = false;
                forTest2 = true;

            } /*else if (SplashScreen.failLoadFlag == false) {
                Toast.makeText(getActivity(), "국토교통부 서버가 불안정합니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "잠시후에 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
                forTest1 = false;
                forTest2 = true;

                Toast.makeText(getActivity(), "모의주행을 종료합니다.", Toast.LENGTH_SHORT).show();
                BaseActivity.sm.setSlidingEnabled(true);
                MainActivity.actionBar.setHomeButtonEnabled(true);
                checkBox.setEnabled(true);


            }*/


        } else if (forTest2) {
            forTest2 = false;
            Toast.makeText(getActivity(), "모의주행을 종료합니다.", Toast.LENGTH_SHORT).show();
            BaseActivity.sm.setSlidingEnabled(true);
            MainActivity.actionBar.setHomeButtonEnabled(true);
            checkBox.setEnabled(true);
        }

    }

    public boolean serviceCheck() {
//        flag = false;
        am = (ActivityManager) getActivity()
                .getSystemService(Context.ACTIVITY_SERVICE);
        rs = am.getRunningServices(150);
        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            if (rsi.service.getClassName().equals(
                    "com.example.namsoo.myslidingmenu.GPSservice")) {
                // 해당 서비스가 돌고 있으면
                Log.e(TAG, "com.example.namsoo.myslidingmenu.GPSservice FOUND");
                flag = true;
                break;
            } else {// Log.d("ROSA", "Class Name : " + rsi.service.getClassName());
                flag = false;
//                Log.e(TAG, i + "");
            }
        }
        return flag;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
        editor = prefs.edit();
        editor.putBoolean("flag", autoFlag);
        editor.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }


}