package com.example.namsoo.myslidingmenu;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

/**
 * Created by 김나영 on 2015-08-11.
 */

public class VMSParsing extends AsyncTask<Void, Void, Void> {
    //안드로이드에서 AsyncTask는 쓰레드관리와 UI Thread의 통신을 원활하게 도와주는 Wrapper class.
    //쓰레드 쓰기위해서 AsyncTask클래스를 상속받은거야


    //해당 고속도로 이름 저장위한 객체선언
    Vector<String> routeName = new Vector<String>();
    //vmsID 저장위한 객체선언
    Vector<String> vmsId = new Vector<String>();
    //vmsMessage 저장위한 객체선언
    Vector<String> vmsMessage = new Vector<String>();



    URL url; //웹사이트 연결위해 url클래스 적용

    //연결할 사이트 주소선택

  //  String uri ="http://data.ex.co.kr/openapi/vms/vmsMessageSrch?key=8571160015&type=xml&vmsId=1000VME00300&updownType=E";
    // String uri ="http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.156524&MaxX=127.165671&MinY=37.573320&MaxY=37.589621&type=ex";

    String uri= "http://data.ex.co.kr/openapi/vms/vmsMessageSrch?key=8571160015&type=xml&routeNo=1000&centerCode=200000&updownType=S";
    //xml에서 읽어드려 저장할 변수들
    String _vmsMessage="", _vmsId="", _routeName="";
    String tagname;

    //제대로 데이터가 읽어졌는지 판단해줄 변수
    boolean flag = false;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    //사이트 접속해서 데이터 추출하는 부분
    @Override
    protected Void doInBackground(Void... params) {

        try{
            //안드로이드에서 xml문서를 읽고 파싱하는 객체 선언
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            //네임스페이스 사용여부
            factory.setNamespaceAware(true);

            //실제 sax형태로 데이터를 파싱하는 객체선언
            XmlPullParser xpp = factory.newPullParser();

            //웹사이트 접속
            url= new URL(uri);

            //웹사이트를 통해서 읽어드린 xml문서를 안드로이드에 저장
            InputStream in = url.openStream();

            //xml문서를 읽고 파싱하는 객체에 넘겨주기
            xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정



            //이벤트 타입을 얻어옴
            int eventType = xpp.getEventType();



            //문서 끝까지 읽으면서 데이터를 추출해
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        tagname=xpp.getName();

                        if(tagname.equals("vmsMessageSrchLists"));
                        else if(tagname.equals("routeName")){
                            xpp.next();
                             _routeName+= xpp.getText();
                        }else if(tagname.equals("vmsId")){
                            xpp.next();
                            _vmsId+=xpp.getText();
                        }else if(tagname.equals("vmsMessage")){
                            xpp.next();
                            _vmsMessage+=xpp.getText();



                            routeName.add(_routeName);
                            vmsId.add(_vmsId);
                            vmsMessage.add(_vmsMessage);

                            //변수초기화
                            _routeName="";
                            _vmsId="";
                            _vmsMessage="";


                        }


                }//if
                //다음 이벤트 대입을 저장하기
                eventType = xpp.next();
            }//while


            flag=true; //xml파일 모두 읽고 필요한 데이터를 추출해서 저장 완료한 상태



        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
