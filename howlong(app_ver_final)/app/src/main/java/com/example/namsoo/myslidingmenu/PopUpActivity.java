package com.example.namsoo.myslidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.ArrayList;


public class PopUpActivity extends Activity {
    public String VIDEO_URL = "";//http://cctvsec.ktict.co.kr/2/t75IPyKGmKStbou7xPeZfzxmOB1t058vSN6PvpqsLOeP+0AMhX198chQO9xc0iHb";
    VideoView videoView;
    String TAG = "PopUpActivity";
    double tempTime = 0.0;
    boolean setting_vibration = BaseActivity.notifications_vibration;
    boolean setting_tts = BaseActivity.notifications_sound;

    ArrayList<String> namevec = new ArrayList<String>();
    ArrayList<String> latvec = new ArrayList<String>();
    ArrayList<String> lonvec = new ArrayList<String>();
    ArrayList<String> urlvec = new ArrayList<String>();


    Double timeValue;
    double lon;
    double lat;


    Location myLocation;
    Location cctv1;
    String bestCCTV[] = {"999999999999999", ""};
    Intent ttsService;
    Intent ttsVmsService;
    Intent getIntent;
    Vibrator v;
    ProgressBarCircularIndeterminate bar;
    TextView time;
    TextView length;
    boolean driveModeFlag;
    public static boolean repeatNoFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          /* blur */
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);


        setContentView(R.layout.activity_pop_up);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //투명배경

        repeatNoFlag = true;
        Intent getintent = getIntent();
        driveModeFlag = getintent.getBooleanExtra("drivemode", false);


        ButtonRectangle btn_finish = (ButtonRectangle) findViewById(R.id.button);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setting_tts == true && setting_vibration == true) {
                    stopService(ttsService);
//                    stopService(ttsVmsService);
//                    v.vibrate(1000);
                } else if (setting_tts == true && setting_vibration == false) {
                    stopService(ttsService);
                } else if (setting_tts == false && setting_vibration == true) {
//                    v.vibrate(1000);
                }
                if (driveModeFlag == true) {

                    Intent intent = new Intent(getApplicationContext(), LaughterTherapyActivity.class);
                    repeatNoFlag = false;
                    startActivity(intent);

                }
                finish();
                Log.e(TAG, "onClick()");
            }
        });
       /* blur */

        Log.e(TAG, "onCreate()");
        getIntent = getIntent();
        timeValue = getIntent.getDoubleExtra("timeValue", 0.0);
        Log.e(TAG, "timeValue :" + timeValue);

        namevec = SplashScreen.namevec;
        lonvec = SplashScreen.lonvec;
        latvec = SplashScreen.latvec;
        urlvec = SplashScreen.urlvec;


        time = (TextView) findViewById(R.id.time);
        length = (TextView) findViewById(R.id.length);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        bar.setVisibility(View.VISIBLE);


        if (SplashScreen.failLoadFlag == false) {
            Toast.makeText(getApplicationContext(), "국토교통부 서버가 불안정합니다.", Toast.LENGTH_SHORT).show();
        } else if (SplashScreen.failLoadFlag == true) {
            //내근처 cctv들고오기
            for (int i = 0; i < 20; i++) {
                //내위치
                try {
                    myLocation = new Location("");
                    myLocation.setLatitude(GPSservice.myLoction.getLatitude());
                    myLocation.setLongitude(GPSservice.myLoction.getLongitude());


                    lon = Double.valueOf(lonvec.get(i).toString());
                    lat = Double.valueOf(latvec.get(i).toString());
                    String name = namevec.get(i);

                    //cctv 지점
                    cctv1 = new Location("");
                    cctv1.setLatitude(lon);
                    cctv1.setLongitude(lat);

                    float distanceInMeters = myLocation.distanceTo(cctv1);
                    if (Float.valueOf(bestCCTV[0]) > distanceInMeters) {
                        bestCCTV[0] = String.valueOf(distanceInMeters);
                        bestCCTV[1] = urlvec.get(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        tempTime = GPSservice.avg_time;


        Log.e(TAG, "Time :" + tempTime);

        time.setTypeface(Typeface.createFromAsset(getAssets(), "popup.ttf"));
        length.setTypeface(Typeface.createFromAsset(getAssets(), "popup.ttf"));


        String temp = GPSservice.avg_time + "";

        if (temp.equals("NaN")) {
            time.setText("서울외곽순환고속도로");

            length.setText("전구간 소통 원활합니다.");

        } else {
            String str = String.format("%.0f", GPSservice.avg_time);

            time.setText("정체 소요 시간 : " + "약 " + str + "분");

            length.setText("정체 구간 길이 : " + (int) GPSservice.sum_len + "m");
        }

        Log.e(TAG, "onCreate()");
        videoView = (VideoView) findViewById(R.id.videoView);


        if (SplashScreen.failLoadFlag == true) {
            videoView.setVideoURI(Uri.parse(bestCCTV[1]));

            // 미디어컨트롤러 추가하는부분
//        MediaController controller = new MediaController(this);
//        videoView.setMediaController(controller);

            // 준비하는 과정을 미리함
            videoView.requestFocus();

            // 동영상이 재생준비가 완료되엇을떄를 알수있는 리스너 (실제 웹에서 영상을 다운받아 출력할때 많이 사용됨)
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                // 동영상 재생준비가 완료된후 호출되는 메서드
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    Log.e(TAG, "onPrepared()");
                    bar.setVisibility(View.GONE);

                    playVideo();


                }
            });

            // 동영상 재생이 완료된걸 알수있는 리스너
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                // 동영상 재생이 완료된후 호출되는 메서드
                public void onCompletion(MediaPlayer player) {
                    Log.e(TAG, "onCompletion()");
                }
            });

        }
        Log.e(TAG, "playVideo()");

        ttsService = new Intent(getApplicationContext(), TTS.class);
        ttsVmsService = new Intent(getApplicationContext(), TTSVMS.class);


        setting_tts = BaseActivity.notifications_sound;
        setting_vibration = BaseActivity.notifications_vibration;

        if (setting_tts == true && setting_vibration == true) {
            Log.e(TAG, "tts and vibration");

            Log.e(TAG, "start tts");
            startService(ttsService);
//            startService(ttsVmsService);
            Log.e(TAG, "end tts");

            Log.e(TAG, "start vibrate");
            v.vibrate(1000);
            Log.e(TAG, "end vibrate");

        } else if (setting_tts == true && setting_vibration == false) {
            Log.e(TAG, "only tts");
            Log.e(TAG, "start tts");
            startService(ttsService);
            Log.e(TAG, "end tts");

        } else if (setting_tts == false && setting_vibration == true) {
            Log.e(TAG, "only vibration");
            Log.e(TAG, "start vibrate");
            v.vibrate(1000);
            Log.e(TAG, "end vibrate");

        } else if (setting_tts == false && setting_vibration == false) {
            Log.d(TAG, "아무것도 안함.");
        }


        new Handler().postDelayed(new Runnable() {
                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                // close this activity
                if (setting_tts == true && setting_vibration == true) {
                    stopService(ttsService);
                    //                    stopService(ttsVmsService);
                    //                    v.vibrate(1000);
                } else if (setting_tts == true && setting_vibration == false) {
                    stopService(ttsService);
                } else if (setting_tts == false && setting_vibration == true) {
                    //                    v.vibrate(1000);
                }
                if (driveModeFlag == true && repeatNoFlag == true) {

                    Intent intent = new Intent(getApplicationContext(), LaughterTherapyActivity.class);
                    startActivity(intent);
                    Log.e(TAG, "here!!!!!!!!!!!!!!!!!!");
                }

                finish();
                //스타트 팝업
            }
        }, 12000);
    }


    private void playVideo() {
        // 비디오를 처음부터 재생할땐 0
        videoView.seekTo(0);
        // 비디오 재생 시작
        videoView.start();

    }


    public void stopServiceMethod(Context context) {
        Intent Service = new Intent(context, TTS.class);
        context.stopService(Service);
        Log.e(TAG, "stopServiceMethod()");
    }


}