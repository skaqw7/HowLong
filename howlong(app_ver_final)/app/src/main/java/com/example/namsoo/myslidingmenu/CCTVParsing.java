package com.example.namsoo.myslidingmenu;

import android.os.AsyncTask;
import android.os.StrictMode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 김나영 on 2015-08-11.
 */
public class CCTVParsing extends AsyncTask<Void, Void, Void> {

    //안드로이드에서 AsyncTask는 쓰레드관리와 UI Thread의 통신을 원활하게 도와주는 Wrapper class.
    //쓰레드 쓰기위해서 AsyncTask클래스를 상속받은거야

//    //cctv이름 저장위한 객체선언
//     Vector<String> namevec = new Vector<String>();
//    //logitude를 저장위한 객체선언
//     List<String> lonvec = new Vector<String>();
//    //latitude를 저장위한 객체선언
//    List<String> latvec = new Vector<String>();
//    //url를 저장위한 객체선언
//    Vector<String> urlvec = new Vector<String>();

    ArrayList<String> namevec = new ArrayList<String>();
    ArrayList<String> latvec = new ArrayList<String>();
    ArrayList<String> lonvec = new ArrayList<String>();
    ArrayList<String> urlvec = new ArrayList<String>();


    URL url; //웹사이트 연결위해 url클래스 적용

    //연결할 사이트 주소선택

    String uri ="http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.106524&MaxX=127.185671&MinY=37.303320&MaxY=37.709621&type=ex";
    // 전체 String uri = "http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.100000&MaxX=128.890000&MinY=34.100000&MaxY=39.100000";
    // 3개 String uri ="http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.156524&MaxX=127.165671&MinY=37.573320&MaxY=37.589621&type=ex";

    //xml에서 읽어드려 저장할 변수들
    String name = "", lon = "", lat = "", cctvurl = "";
    String tagname;

    //제대로 데이터가 읽어졌는지 판단해줄 변수
    boolean flag = false;




    //db
    // DbManager db= new DbManager(this);

//
//    public CCTVParsing(Context context)
//    {
//
//    }
//


    //사이트 접속해서 데이터 추출하는 부분
    @Override
    protected Void doInBackground(Void... params) {






        try {
            //안드로이드에서 xml문서를 읽고 파싱하는 객체 선언
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

            //네임스페이스 사용여부
            factory.setNamespaceAware(true);

            //실제 sax형태로 데이터를 파싱하는 객체선언
            XmlPullParser xpp = factory.newPullParser();

            //웹사이트 접속
            url = new URL(uri);

            //웹사이트를 통해서 읽어드린 xml문서를 안드로이드에 저장
            InputStream in = url.openStream();

            //xml문서를 읽고 파싱하는 객체에 넘겨주기
            xpp.setInput(in, "UTF-8"); //xml문서의 인코딩 정확히 지정

            //item태그 안이라면
            boolean isInItemTag = false;

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
                            //  latvec.add(Double.valueOf(lat));
                            //  lonvec.add(Double.valueOf(lon));
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
                            //   latvec.add(Double.valueOf(lat));
                            //  lonvec.add(Double.valueOf(lon));
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
                                //latvec.add(Double.valueOf(lat));
                                //lonvec.add(Double.valueOf(lon));
                                latvec.add(lat);
                                lonvec.add(lon);
                                //변수초기화
                                name = "";
                                cctvurl = "";
                                lat = "";
                                lon = "";

                                isInItemTag = false;
                            }
                        }//if

                }//if
                //다음 이벤트 대입을 저장하기
                eventType = xpp.next();
            }//while


            flag = true; //xml파일 모두 읽고 필요한 데이터를 추출해서 저장 완료한 상태


        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
    }
}