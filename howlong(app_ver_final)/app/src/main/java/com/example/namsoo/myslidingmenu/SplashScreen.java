package com.example.namsoo.myslidingmenu;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.namsoo.myslidingmenu.tutorial.ViewPagerStyle2Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by SangWook on 2015-08-04.
 */

public class SplashScreen extends Activity {

    //데이터 저장위한 객체선언
    InputStream in;
    String TAG = "SplashScreen";
    boolean success = false;

    static boolean failLoadFlag = true;

    static ArrayList<String> namevec = new ArrayList<String>();
    static ArrayList<String> latvec = new ArrayList<String>();
    static ArrayList<String> lonvec = new ArrayList<String>();
    static ArrayList<String> urlvec = new ArrayList<String>();

    URL url; //웹사이트 연결위해 url클래스 적용
    //연결할 사이트 주소선택
    String uri = "http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.106524&MaxX=127.185671&MinY=37.303320&MaxY=37.709621&type=ex";

    //xml에서 읽어드려 저장할 변수들
    String name = "", lon = "", lat = "", cctvurl = "";
    String tagname;

    //제대로 데이터가 읽어졌는지 판단해줄 변수
    boolean flag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StrictMode.enableDefaults();

        if (serviceChk() == false) { //gps service가 없느 경우 : ex) 최초 실행
            new LoadAssync().execute();
        } else if (serviceChk() == true) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
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
                serviceChkflag = true;
                break;
            } else {
                serviceChkflag = false;
            }
        }
        return serviceChkflag;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }







    private class LoadAssync extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "LoadAssync()");

        }

        //사이트 접속해서 데이터 추출하는 부분
        @Override
        protected Void doInBackground(String... params) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                //안드로이드에서 xml문서를 읽고 파싱하는 객체 선언
                URLConnection con;

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
                    con.setConnectTimeout(4000);
                    con.setReadTimeout(4000);
                    in = con.getInputStream();

                    success = true;
                } catch (Exception e) {
                    success = false;
                    return null;
                }

                //xml문서를 읽고 파싱하는 객체에 넘겨주기
                xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정

                //이벤트 타입을 얻어옴
                int eventType = xpp.getEventType();

                //문서 끝까지 읽으면서 데이터를 추출해
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:

                            break;
                        case XmlPullParser.START_TAG:
                            tagname = xpp.getName();

                            if (tagname.equals("vmsMessageSrchLists")) ;
                            else if (tagname.equals("cctvurl")) {
                                xpp.next();
                                cctvurl += xpp.getText();
                            } else if (tagname.equals("coordy")) {
                                xpp.next();
                                lon += xpp.getText();
                            } else if (tagname.equals("cctvname")) {
                                xpp.next();
                                name += xpp.getText();
                            } else if (tagname.equals("coordx")) {
                                xpp.next();
                                lat += xpp.getText();

                                namevec.add(name);
                                urlvec.add(cctvurl);
                                latvec.add(lat);
                                lonvec.add(lon);

                                //변수초기화
                                name = "";
                                cctvurl = "";
                                lat = "";
                                lon = "";

                            } else if (tagname.equals("/data")) {
                                namevec.add(name);
                                urlvec.add(cctvurl);
                                latvec.add(lat);
                                lonvec.add(lon);
                                //변수초기화
                                name = "";
                                cctvurl = "";
                                lat = "";
                                lon = "";


                            } else if (eventType == XmlPullParser.END_TAG) {
                                tagname = xpp.getName();

                                //endtag일 경우에만 백터에 저장하기
                                if (tagname.equals("data")) {
                                    namevec.add(name);
                                    urlvec.add(cctvurl);
                                    latvec.add(lat);
                                    lonvec.add(lon);
                                    //변수초기화
                                    name = "";
                                    cctvurl = "";
                                    lat = "";
                                    lon = "";

                                }
                            }//if
                    }//if
                    //다음 이벤트 대입을 저장하기
                    eventType = xpp.next();
                }//while
                Log.d(TAG, "parsing : 파싱 완료");
                flag = true; //xml파일 모두 읽고 필요한 데이터를 추출해서 저장 완료한 상태
                Log.d(TAG, "parsing : Sleep");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void unused) {

            if (success) {

                Intent intent = new Intent(getApplicationContext(), ViewPagerStyle2Activity.class);
                intent.putExtra("mode", 1);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, 0);
                Log.d(TAG, "Async : success");
                failLoadFlag = true;

                finish();

            } else if (!success) {
                Toast.makeText(getApplicationContext(), "국토교통부 서버가 불안정합니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "잠시후에 다시 실행해주세요.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ViewPagerStyle2Activity.class);
                        intent.putExtra("mode", 1);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up, 0);
                        failLoadFlag = false;
                        finish();
                        //스타트 팝업
                    }
                }, 3000);
                Log.d(TAG, "Async : fail");
            }
        }
    }




}