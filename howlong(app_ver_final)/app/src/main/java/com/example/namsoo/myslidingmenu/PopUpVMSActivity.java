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
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Vector;


public class PopUpVMSActivity extends Activity {


    String TAG = "PopUpVMSActivity";
    boolean success = false;


    //해당 고속도로 이름 저장위한 객체선언
    Vector<String> routeName = new Vector<String>();
    //vmsID 저장위한 객체선언
    Vector<String> vmsId = new Vector<String>();
    //vmsMessage 저장위한 객체선언
    Vector<String> vmsMessage = new Vector<String>();
    ArrayList<String> vmsMessageTemp;
    Double timeValue;

    URL url; //웹사이트 연결위해 url클래스 적용
    String uri = "http://data.ex.co.kr/openapi/vms/vmsMessageSrch?key=8571160015&type=xml&routeNo=1000&centerCode=200000&updownType=S";
    InputStream in;
    String _vmsMessage = "", _vmsId = "", _routeName = "";
    String tagname;


    Intent ttsVMSService;

    Vibrator v;
    ProgressBarCircularIndeterminate bar;
    TextView routeName_textview;
    static TextView vmsMessage_textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          /* blur */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent getintent = getIntent();
        boolean drivetestmodeflag = getintent.getBooleanExtra("drivetestmode", false);

        if(drivetestmodeflag == true)
        {
            IntroFragment.forTest1 = true;
            Log.e(TAG, "drivetestmode getintent");
        }
        else if (drivetestmodeflag == false)
        {
            Log.e(TAG, "drivetestmode getintent false");
        }



        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);


        setContentView(R.layout.activity_pop_up_vms);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //투명배경

        ButtonRectangle btn_finish = (ButtonRectangle) findViewById(R.id.button);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(ttsVMSService);
                finish();
                Log.e(TAG, "onClick()");
            }
        });
       /* blur */

        Log.e(TAG, "onCreate()");
/*        getIntent = getIntent();
        timeValue = getIntent.getDoubleExtra("timeValue", 0.0);*/
        Log.e(TAG, "timeValue :" + timeValue);

 /*       namevec = SplashScreen.namevec;
        lonvec = SplashScreen.lonvec;
        latvec = SplashScreen.latvec;
        urlvec = SplashScreen.urlvec;*/


        routeName_textview = (TextView) findViewById(R.id.text_routeName);
        vmsMessage_textview = (TextView) findViewById(R.id.text_vmsMessage);

        routeName_textview.setTypeface(Typeface.createFromAsset(getAssets(), "popup.ttf"));
        vmsMessage_textview.setTypeface(Typeface.createFromAsset(getAssets(), "popup.ttf"));


        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);


     /*   //내근처 cctv들고오기
        for (int i = 0; i < 20; i++) {
            //내위치
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
        }


        tempTime = GPSservice.avg_time;


        Log.e(TAG, "Time :" + tempTime);

        time.setTypeface(Typeface.createFromAsset(getAssets(), "popup.ttf"));
        length.setTypeface(Typeface.createFromAsset(getAssets(), "popup.ttf"));
        String str = String.format("%.0f", GPSservice.avg_time);

        time.setText("정체 소요 시간 : " + "약 " + str + "분");

        length.setText("정체 구간 길이 : " + (int) GPSservice.sum_len + "m");
*/

        Log.e(TAG, "onCreate()");
/*
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(bestCCTV[1]));
*/
        // 미디어컨트롤러 추가하는부분
//        MediaController controller = new MediaController(this);
//        videoView.setMediaController(controller);

        // 준비하는 과정을 미리함
//        videoView.requestFocus();

        // 동영상이 재생준비가 완료되엇을떄를 알수있는 리스너 (실제 웹에서 영상을 다운받아 출력할때 많이 사용됨)
/*
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
*/
        ttsVMSService = new Intent(getApplicationContext(), TTSVMS.class);
        new LoadAssync().execute(null, null, null);


//        startService(ttsVMSService);
        startService(ttsVMSService);

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
                startService(ttsVMSService);
                finish();
            }
        }, 15000);
    }




 /*   private void playVideo() {
        // 비디오를 처음부터 재생할땐 0
        videoView.seekTo(0);
        // 비디오 재생 시작
        videoView.start();

    }*/

/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (setting_tts == true) {
            stopServiceMethod(this);
        }
        Log.e(TAG, "onDestroy()");

    }*/

    public void stopServiceMethod(Context context) {
        Intent Service = new Intent(context, TTSVMS.class);
        context.stopService(Service);
        Log.e(TAG, "stopServiceMethod()");
    }


    /*실패!! 개구려*/
    private class LoadAssync extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
            Log.d(TAG, "LoadAssync()");
        }

        //사이트 접속해서 데이터 추출하는 부분
        @Override
        protected Void doInBackground(String... params) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {


                URLConnection con;

                //안드로이드에서 xml문서를 읽고 파싱하는 객체 선언
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                Log.d(TAG, "doInBackground() : 1");

                //네임스페이스 사용여부
                factory.setNamespaceAware(true);
                Log.d(TAG, "doInBackground() : 2");

                //실제 sax형태로 데이터를 파싱하는 객체선언
                XmlPullParser xpp = factory.newPullParser();
                Log.d(TAG, "doInBackground() : 3");

                //웹사이트 접속
                    url = new URL(uri);
                Log.d(TAG, "doInBackground() : 4");


                //웹사이트를 통해서 읽어드린 xml문서를 안드로이드에 저장

                try {
                    con = url.openConnection();
                    in = con.getInputStream();
                    success = true;
                } catch (Exception e) {
                    Log.d(TAG, "url Check 123");
                    success = false;
                    return null;
                }


//                MatrixTime(3000);

                Log.d(TAG, "doInBackground() : 5");

                //xml문서를 읽고 파싱하는 객체에 넘겨주기
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정
                Log.d(TAG, "doInBackground() : 6");


                //이벤트 타입을 얻어옴
                int eventType = xpp.getEventType();
                Log.d(TAG, "doInBackground() : 7");


                //문서 끝까지 읽으면서 데이터를 추출해

                //문서 끝까지 읽으면서 데이터를 추출해
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:

                            break;
                        case XmlPullParser.START_TAG:
                            tagname = xpp.getName();

                            if (tagname.equals("vmsMessageSrchLists")) ;
                            else if (tagname.equals("routeName")) {
                                xpp.next();
                                _routeName += xpp.getText();
                            } else if (tagname.equals("vmsId")) {
                                xpp.next();
                                _vmsId += xpp.getText();
                            } else if (tagname.equals("vmsMessage")) {
                                xpp.next();
                                _vmsMessage += xpp.getText();


                                routeName.add(_routeName);
                                vmsId.add(_vmsId);
                                vmsMessage.add(_vmsMessage);

                                //변수초기화
                                _routeName = "";
                                _vmsId = "";
                                _vmsMessage = "";


                            }


                    }//if
                    //다음 이벤트 대입을 저장하기
                    eventType = xpp.next();
                }//while

                Log.d(TAG, "doInBackground() : 파싱 완료");





            /*    namevec = cctvparsing.namevec;
                urlvec = cctvparsing.urlvec;
                lonvec = cctvparsing.lonvec;
                latvec = cctvparsing.latvec;*/
//                Thread.sleep(1000);


                Log.d(TAG, "doInBackground()");
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(final Void unused) {

            bar.setVisibility(View.GONE);
            Log.d(TAG, "success: " + success);


            if (success) {
                vmsMessageTemp = new ArrayList<String>();
//            routeName_textview.setText(routeName.get(1));
//            vmsMessage_textview.setText(vmsMessage.get(1));


                for (int i = 0; i < routeName.size(); i++) {
                    if (vmsMessage.get(i).contains("정체") || vmsMessage.get(i).contains("사고") || vmsMessage.get(i).contains("원활") || vmsMessage.get(i).contains("공사") || vmsMessage.get(i).contains("작업")) {
//                    adapter.add(vmsMessage.get(i));
                        vmsMessageTemp.add(vmsMessage.get(i));

                        //   adapter.add(new SampleItem("도로전광판", R.drawable.display_icon));
                        // text_vmsMessage.setBackgroundDrawable(getResources().getDrawable(R.drawable.show_vms));
                        // text_vmsMessage.append(routeName.get(i).toString() + "\n" + vmsId.get(i).toString() + "\n" + vmsMessage.get(i).toString() + "\n\n");

                    }

                }

            /*    if (vmsMessageTemp.size() != 0) {
                    routeName_textview.setText(routeName.get(0));
                    vmsMessage_textview.setText(vmsMessageTemp.get(0));
                }*/

                if (vmsMessageTemp.size() != 0) {


                    if (vmsMessageTemp.size() == 1) {
                        vmsMessage_textview.setTextSize(25);
                    } else if (vmsMessageTemp.size() == 2) {
                        vmsMessage_textview.setTextSize(25);
                    } else if (vmsMessageTemp.size() >= 3) {
                        vmsMessage_textview.setTextSize(25);
                    }


                    for (int i = 0; i < vmsMessageTemp.size(); i++) {
                        if (i == 3) {
                            break;
                        }
                        routeName_textview.setText(routeName.get(i));
                        vmsMessage_textview.append( "\n" + vmsMessageTemp.get(i));

                    }
                } else if (vmsMessageTemp.size() == 0) {
                    routeName_textview.setText("주요 전광판");
                    vmsMessage_textview.setTextSize(25);
                    vmsMessage_textview.setText("정체, 사고, 공사 해당하는\n전광판이 없습니다.");
                }

                //   finish();
                //  stopService(ttsService);
                Log.d(TAG, "onPostExecute() > finish");

            } else {
                routeName_textview.setText("주요 전광판");
                vmsMessage_textview.setTextSize(25);
                vmsMessage_textview.setText("정체, 사고, 공사 해당하는\n전광판이 없습니다.");
            }
        }
    }

    public void MatrixTime(int delayTime) {
        long saveTime = System.currentTimeMillis();
        long currTime = 0;
        while (currTime - saveTime < delayTime) {
            currTime = System.currentTimeMillis();
        }
    }

}
