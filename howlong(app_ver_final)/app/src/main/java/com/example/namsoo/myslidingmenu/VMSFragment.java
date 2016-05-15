package com.example.namsoo.myslidingmenu;

/**
 * Created by namsoo on 2015-08-09.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

public class VMSFragment extends Fragment {

    //안드로이드에서 AsyncTask는 쓰레드관리와 UI Thread의 통신을 원활하게 도와주는 Wrapper class.
    //쓰레드 쓰기위해서 AsyncTask클래스를 상속받은거야


    //해당 고속도로 이름 저장위한 객체선언
    Vector<String> routeName = new Vector<String>();
    //vmsID 저장위한 객체선언
    Vector<String> vmsId = new Vector<String>();
    //vmsMessage 저장위한 객체선언
    Vector<String> vmsMessage = new Vector<String>();
    InputStream in;


    //    TextView text_vmsMessage;
    ListView vmsListView;
    ArrayAdapter adapter;
    Vector<String> init = new Vector<String>();

    URL url; //웹사이트 연결위해 url클래스 적용


    //Button allVms;
    //Button importanceVms;

    //btn2개
    ButtonFlat allVms;
    ButtonFlat importanceVms;
    TextView routeTv;

    //연결할 사이트 주소선택

    //  String uri ="http://data.ex.co.kr/openapi/vms/vmsMessageSrch?key=8571160015&type=xml&vmsId=1000VME00300&updownType=E";
    // String uri ="http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.156524&MaxX=127.165671&MinY=37.573320&MaxY=37.589621&type=ex";

    String uri = "http://data.ex.co.kr/openapi/vms/vmsMessageSrch?key=8571160015&type=xml&routeNo=1000&centerCode=200000&updownType=S";
    //xml에서 읽어드려 저장할 변수들
    String _vmsMessage = "", _vmsId = "", _routeName = "";
    String tagname;

    //제대로 데이터가 읽어졌는지 판단해줄 변수
    boolean flag = false;

    String TAG = "VMS";


    ProgressBarCircularIndeterminate bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vms, container, false);

        bar = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressBarCircularIndeterminate);

        //TextView text_routeName = (TextView)v.findViewById(R.id.text_routeName);
        // text_vmsMessage = (TextView) v.findViewById(R.id.text_vmsMessage);

        routeTv = (TextView) v.findViewById(R.id.routeTv);
        allVms = (ButtonFlat) v.findViewById(R.id.btn_allVms);
        importanceVms = (ButtonFlat) v.findViewById(R.id.btn_importanceVms);
        allVms.setVisibility(View.INVISIBLE);
        importanceVms.setVisibility(View.INVISIBLE);


        Log.d(TAG, "1");

        new LoadAssync().execute(null, null, null);
        Log.d(TAG, "2");


        //  for(int i=0; i<vmsMessage.size(); i++){
        //    text_vmsMessage.setText(routeName.get(i).toString()+vmsId.get(i).toString()+vmsMessage.get(i).toString());
        //}

        // text_vmsMessage.setText(vmsMessage.toString());


        Log.d(TAG, "6");
        vmsListView = (ListView) v.findViewById(R.id.vmsView);
//        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, init);
        adapter = new ArrayAdapter(getActivity(), R.layout.custom_list, init);

        vmsListView.setAdapter(adapter);


        return v;
    }


    private class LoadAssync extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "LoadAssync()");
            bar.setVisibility(View.VISIBLE);

        }

        //사이트 접속해서 데이터 추출하는 부분
        @Override
        protected Void doInBackground(String... params) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
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

                in = url.openStream();

                MatrixTime(3000);

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
                Thread.sleep(1000);


                Log.d(TAG, "doInBackground()");
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(final Void unused) {


            Log.d(TAG, "onPostExecute() > MainActivity");
            bar.setVisibility(View.GONE);
            routeTv.setText(routeName.get(1));
            allVms.setVisibility(View.VISIBLE);
            importanceVms.setVisibility(View.VISIBLE);

            // text_vmsMessage.setVerticalScrollBarEnabled(true);
            //  text_vmsMessage.setMovementMethod(new ScrollingMovementMethod());


            adapter.clear();
            for (int i = 0; i < routeName.size(); i++) {
                if (!vmsMessage.get(i).contains("null")) {

                    adapter.add(vmsMessage.get(i));
                    //text_vmsMessage.append( "\n" + vmsMessage.get(i).toString() + "\n\n");
                }
            }

            importanceVms.setBackgroundColor(Color.parseColor("#ffe5e0de"));
            allVms.setBackgroundColor(Color.parseColor("#1E88E5"));

            allVms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // text_vmsMessage.setText("");
                    adapter.clear();
                    allVms.setBackgroundColor(Color.parseColor("#1E88E5"));
                    importanceVms.setBackgroundColor(Color.parseColor("#ffe5e0de"));

                    for (int i = 0; i < routeName.size(); i++) {
                        if (!vmsMessage.get(i).contains("null")) {

                            adapter.add(vmsMessage.get(i));
                            //text_vmsMessage.append( "\n" + vmsMessage.get(i).toString() + "\n\n");
                        }

                    }
                }
            });

            importanceVms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //text_vmsMessage.setText("");
                    adapter.clear();
                    allVms.setBackgroundColor(Color.parseColor("#ffe5e0de"));
                    importanceVms.setBackgroundColor(Color.parseColor("#1E88E5"));

                    for (int i = 0; i < routeName.size(); i++) {
                        if (vmsMessage.get(i).contains("정체") || vmsMessage.get(i).contains("사고") || vmsMessage.get(i).contains("원활") || vmsMessage.get(i).contains("공사") || vmsMessage.get(i).contains("작업")) {
                            adapter.add(vmsMessage.get(i));
                            //   adapter.add(new SampleItem("도로전광판", R.drawable.display_icon));
                            // text_vmsMessage.setBackgroundDrawable(getResources().getDrawable(R.drawable.show_vms));
                            // text_vmsMessage.append(routeName.get(i).toString() + "\n" + vmsId.get(i).toString() + "\n" + vmsMessage.get(i).toString() + "\n\n");

                        }

                    }
                }
            });

            //text_vmsMessage.setText(routeName.get(2).toString() + "\n" + vmsId.get(2).toString() + "\n" + vmsMessage.get(2).toString());

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