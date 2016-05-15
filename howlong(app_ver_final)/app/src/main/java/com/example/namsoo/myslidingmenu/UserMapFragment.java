package com.example.namsoo.myslidingmenu;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.widgets.Dialog;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;


/**
 * Created by SangWook on 2015-08-03.
 */
public class UserMapFragment extends Fragment {
    NMapView mapView;
    private NMapContext mMapContext;
    NMapController mMapController;
    NMapLocationManager mMapLocationManager;
    NMapView.OnMapStateChangeListener onMapViewStateChangeListener;
    NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener;
    NMapMyLocationOverlay mMyLocationOverlay;
    NMapCompassManager mMapCompassManager;
    NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapOverlayManager mOverlayManager;
    double valueOflatitude;
    double valueOflongitude;
    double valueOfvelocity;
    boolean StartFlag = true;
    String TAG = "UserMapFragment";

    //데이터 저장위한 객체선언
    //  Vector<String> namevec = new Vector<String>();
    //  Vector<Double> latvec = new Vector<Double>();
    //  Vector<Double> lonvec = new Vector<Double>();
    // Vector<String> urlvec = new Vector<String>();


    ArrayList<String> namevec = new ArrayList<String>();
    ArrayList<String> latvec = new ArrayList<String>();
    ArrayList<String> lonvec = new ArrayList<String>();
    ArrayList<String> urlvec = new ArrayList<String>();

    //int markerId = NMapPOIflagType.GREENJAM;


    //데이터 뽑아오는 클래스 선언
    CCTVParsing cctvparsing = new CCTVParsing();

    DbManager db;
    DbManager db2;
    //btn2개
    ButtonRectangle downCCTV;
    ButtonRectangle upCCTV;

    /**
     * Fragment5에 포함된 NMapView 객체를 반환함
     */
    private NMapView findMapView(View v) {

        if (!(v instanceof ViewGroup)) {
            return null;
        }

        ViewGroup vg = (ViewGroup) v;
        if (vg instanceof NMapView) {
            return (NMapView) vg;
        }

        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);
            if (!(child instanceof ViewGroup)) {
                continue;
            }

            NMapView mapView = findMapView(child);
            if (mapView != null) {
                return mapView;
            }
        }
        return null;
    }


    public static NMapView setMapV(NMapView mapv) {
        return mapv;
    }


    public static UserMapFragment newInstance(Double lat, Double lon, Double vel) {
        UserMapFragment fragment = new UserMapFragment();


        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lon", lon);
        args.putDouble("vel", vel);
        fragment.setArguments(args);

        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = new DbManager(getActivity(), 0);
        db2 = new DbManager(getActivity(), 2);
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

//mapView = (NMapView) rootView.findViewById(R.id.usermapview);


        valueOflatitude = getArguments() != null ? getArguments().getDouble("lat", 0.00000) : 10000.999999;
        valueOflongitude = getArguments() != null ? getArguments().getDouble("lon" +
                "", 0.00000) : 10000.999999;
        valueOfvelocity = getArguments() != null ? getArguments().getDouble("vel", 0.00000) : 10000.999999;


        return rootView;


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Fragment에 포함된 NMapView 객체 찾기
        NMapView mapView = findMapView(super.getView());
        if (mapView == null) {
            throw new IllegalArgumentException("NMapFragment dose not have an instance of NMapView.");
        }

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.
        mMapContext.setupMapView(mapView);

        mapView.setApiKey("97794edc9e45da4478b9cd5852cb2fda");
        mapView.setClickable(true);
        mapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

        mapView.setBuiltInZoomControls(true, null);
        mMapController = mapView.getMapController();
        mMapController.setMapCenter(valueOflongitude, valueOflatitude, 8);
        mMapController.setMapViewMode(NMapView.VIEW_MODE_TRAFFIC);

        mMapController.setMapViewTrafficMode(true);


        mMapLocationManager = new NMapLocationManager(getActivity());
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);


// create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());

// create overlay manager
        mOverlayManager = new NMapOverlayManager(getActivity(), mapView, mMapViewerResourceProvider);


        //slpashScreen으로부터 값 받기
        Bundle extra = getArguments();


        namevec = SplashScreen.namevec;
        lonvec = SplashScreen.lonvec;
        latvec = SplashScreen.latvec;
        urlvec = SplashScreen.urlvec;

        //Toast.makeText(getActivity(), aa,Toast.LENGTH_LONG).show();


        final int markerGreen = NMapPOIflagType.GREENJAM;
        final int markerYellow = NMapPOIflagType.YELLOWJAM;
        final int markerRed = NMapPOIflagType.REDJAM;
        final int markerPink = NMapPOIflagType.SPOT;
        final int markerBlack = NMapPOIflagType.BLACKJAM;


        final double db_speed[] = db.speed;
        final double db_lon[] = db.x;
        final double db_lat[] = db.y;

        final double db2_speed[] = db2.speed;
        final double db2_lon[] = db2.x;
        final double db2_lat[] = db2.y;

        for (int i = 0; i < namevec.size() && i != 18 && i != 17; i++) {
            // double lon = lonvec.elementAt(i);
            // double lat = latvec.elementAt(i);

            double lon = Double.valueOf(lonvec.get(i).toString());
            double lat = Double.valueOf(latvec.get(i).toString());
            NMapPOIdata poiData = new NMapPOIdata(110, mMapViewerResourceProvider);
            poiData.beginPOIdata(110);


            poiData.addPOIitem(lat, lon, namevec.get(i), markerBlack, 0);


//        Log.e(TAG, "4");
            poiData.endPOIdata();
//        Log.e(TAG, "5");
//// create POI data overlay
            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
//
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);


        }


        downCCTV = (ButtonRectangle) getView().findViewById(R.id.btn_downCCTV);
        //   downCCTV =(Button) mapView.findViewById(R.id.btn_downCCTV);

        upCCTV = (ButtonRectangle) getView().findViewById(R.id.btn_upCCTV);

        upCCTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)


            {


                //

                if (SplashScreen.failLoadFlag == true) {
                    NMapPOIdata poiData = new NMapPOIdata(110, mMapViewerResourceProvider);
                    poiData.beginPOIdata(110);


                    for (int i = 0; i < 35; i++) {

                        for (int j = 0; j < 20; j++) {

                            if (db2_speed[2] < 40.0) {//정체

                                //db에 저장된 정체구간의 지점
                                Location db2 = new Location("");
                                db2.setLatitude(db_lat[i]);
                                db2.setLongitude(db_lon[i]);


                                double lon = Double.valueOf(lonvec.get(j).toString());
                                double lat = Double.valueOf(latvec.get(j).toString());
                                String name = namevec.get(j);

                                //cctv 지점
                                Location cctv1 = new Location("");
                                cctv1.setLatitude(lat);
                                cctv1.setLongitude(lon);

                                float distanceInMeters = db2.distanceTo(cctv1);
                                if (distanceInMeters < 1000) { //red찍기
                                    poiData.addPOIitem(lat, lon, namevec.get(j), markerRed, 0);

                                    Log.e(TAG, "1");

                                }

                            } else if (40 <= db2_speed[i] && db2_speed[i] < 80) {//노란지점
                                //db2에 저장된 정체구간의 지점
                                Location db2 = new Location("");
                                db2.setLatitude(db2_lat[i]);
                                db2.setLongitude(db2_lon[i]);


                                double lon = Double.valueOf(lonvec.get(j).toString());
                                double lat = Double.valueOf(latvec.get(j).toString());
                                String name = namevec.get(j);

                                //cctv 지점
                                Location cctv1 = new Location("");
                                cctv1.setLatitude(lat);
                                cctv1.setLongitude(lon);

                                float distanceInMeters = db2.distanceTo(cctv1);
                                if (distanceInMeters < 1000) { //노랑찍기
                                    poiData.addPOIitem(lat, lon, namevec.get(j), markerYellow, 0);

                                    Log.e(TAG, "2");
                                }

                            } else if (db2_speed[i] > 80) {//정체아님

                                //db에 저장된 정체구간의 지점
                                Location db2 = new Location("");
                                db2.setLatitude(db2_lat[i]);
                                db2.setLongitude(db2_lon[i]);


                                double lon = Double.valueOf(lonvec.get(j).toString());
                                double lat = Double.valueOf(latvec.get(j).toString());
                                String name = namevec.get(j);

                                //cctv 지점
                                Location cctv1 = new Location("");
                                cctv1.setLatitude(lat);
                                cctv1.setLongitude(lon);

                                float distanceInMeters = db2.distanceTo(cctv1);
                                if (distanceInMeters < 1000) { //초록찍기
                                    poiData.addPOIitem(lat, lon, namevec.get(j), markerGreen, 0);
                                }
                            }
                        }

                    }
                    Log.d("SplashScreen", SplashScreen.failLoadFlag+ "");
                    poiData.endPOIdata();
//// create POI data overlay
                    NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                    poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
                } else if (SplashScreen.failLoadFlag == false) {

                    Log.d("SplashScreen", SplashScreen.failLoadFlag+ "");
                    Toast.makeText(getActivity(), "국토교통부 서버가 불안정합니다.", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "잠시후에 다시 실행해주세요.", Toast.LENGTH_LONG).show();
                }
                //
            }
        });
        downCCTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SplashScreen.failLoadFlag == true) {

                    //Toast.makeText(getActivity(),String.valueOf(db_speed[8]),Toast.LENGTH_LONG).show();

                    //  poiData.removeAllPOIdata();


                    NMapPOIdata poiData = new NMapPOIdata(110, mMapViewerResourceProvider);
                    poiData.beginPOIdata(110);


                    for (int i = 0; i < 35; i++) {

                        for (int j = 0; j < 20; j++) {

                            if (db_speed[i] < 40.0) {//정체

                                //db에 저장된 정체구간의 지점
                                Location db1 = new Location("");
                                db1.setLatitude(db_lat[i]);
                                db1.setLongitude(db_lon[i]);


                                double lon = Double.valueOf(lonvec.get(j).toString());
                                double lat = Double.valueOf(latvec.get(j).toString());
                                String name = namevec.get(j);

                                //cctv 지점
                                Location cctv1 = new Location("");
                                cctv1.setLatitude(lat);
                                cctv1.setLongitude(lon);

                                float distanceInMeters = db1.distanceTo(cctv1);
                                if (distanceInMeters < 1000) { //red찍기
                                    poiData.addPOIitem(lat, lon, namevec.get(j), markerRed, 0);

                                    Log.e(TAG, "1");

                                }

                            } else if (40 <= db_speed[i] && db_speed[i] < 80) {//노란지점
                                //db에 저장된 정체구간의 지점
                                Location db1 = new Location("");
                                db1.setLatitude(db_lat[i]);
                                db1.setLongitude(db_lon[i]);


                                double lon = Double.valueOf(lonvec.get(j).toString());
                                double lat = Double.valueOf(latvec.get(j).toString());
                                String name = namevec.get(j);

                                //cctv 지점
                                Location cctv1 = new Location("");
                                cctv1.setLatitude(lat);
                                cctv1.setLongitude(lon);

                                float distanceInMeters = db1.distanceTo(cctv1);
                                if (distanceInMeters < 1000) { //노랑찍기
                                    poiData.addPOIitem(lat, lon, namevec.get(j), markerYellow, 0);

                                    Log.e(TAG, "2");
                                }

                            } else if (db_speed[i] > 80) {//정체아님

                                //db에 저장된 정체구간의 지점
                                Location db1 = new Location("");
                                db1.setLatitude(db_lat[i]);
                                db1.setLongitude(db_lon[i]);


                                double lon = Double.valueOf(lonvec.get(j).toString());
                                double lat = Double.valueOf(latvec.get(j).toString());
                                String name = namevec.get(j);

                                //cctv 지점
                                Location cctv1 = new Location("");
                                cctv1.setLatitude(lat);
                                cctv1.setLongitude(lon);

                                float distanceInMeters = db1.distanceTo(cctv1);
                                if (distanceInMeters < 1000) { //초록찍기
                                    poiData.addPOIitem(lat, lon, namevec.get(j), markerGreen, 0);
                                }
                            }
                        }
                    }
                    Log.d("SplashScreen", SplashScreen.failLoadFlag+ "");

                    poiData.endPOIdata();
//// create POI data overlay
                    NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                    poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);


                } else if (SplashScreen.failLoadFlag == false) {
                    Log.d("SplashScreen", SplashScreen.failLoadFlag+ "");

                    Toast.makeText(getActivity(), "국토교통부 서버가 불안정합니다.", Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(), "잠시후에 다시 실행해주세요.", Toast.LENGTH_LONG).show();

                }


            }
        });
        // create my location overlay
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

        //    startMyLocation();
    }


    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
        }

        //cctv 아이콘 클릭했을시
        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            // Toast.makeText(getActivity(), "뿌려라" + item.getTitle(), Toast.LENGTH_LONG).show();
            try {
                Intent intent = new Intent(getActivity(), PopUpCCTVActivity.class);
                // Toast.makeText(getActivity(),item.getTitle(),Toast.LENGTH_LONG).show();
                // i.putExtra("title", item.getTitle());
                StartFlag = false;
                int selectCCTVIndex = -1;
                for (int i = 0; i < 100; i++) {
                    if (namevec.get(i).toString().equals(item.getTitle())) {
                        selectCCTVIndex = i;
                        break;
                    }
                }
                intent.putExtra("url", urlvec.get(selectCCTVIndex).toString());
                startActivity(intent);
            } catch (Exception e) {
            }
        }
    };


    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener =
            new NMapLocationManager.OnLocationChangeListener() {

                @Override
                public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                    if (mMapController != null) {
                        mMapController.animateTo(nGeoPoint);
                        //      Toast.makeText(getActivity(), nGeoPoint.getLatitude() + " / " + nGeoPoint.getLongitude(), Toast.LENGTH_LONG).show();

                    }
                    return true;
                }

                @Override
                public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {

                }

                @Override
                public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                    stopMyLocation();
                }
            };


    void startMyLocation() {
        if (mMapLocationManager.isMyLocationEnabled()) {


            if (!mapView.isAutoRotateEnabled()) {

                Log.e(TAG, "7");
                mMyLocationOverlay.setCompassHeadingVisible(true);
                mMapCompassManager.enableCompass();
                mapView.setAutoRotateEnabled(true, false);
                Log.e(TAG, "Naver startMyLocation");

            }

            mapView.invalidate();


        } else

        {


            Boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false);   //현재 위치 탐색 시작

            if (!isMyLocationEnabled) { //위치 탐색이 불가능하면
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

                return;


            }
        }
//        Toast.makeText(getActivity(), "Naver GPS 시작", Toast.LENGTH_LONG).show();


    }

    private View.OnClickListener onAcceptButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

         /*   Boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(false);
            if(isMyLocationEnabled == true)
            {
                startMyLocation();
            }*/
        }
    };
    private View.OnClickListener onCancelButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    void stopMyLocation() {
        mMapLocationManager.disableMyLocation();  //현재 위치 탐색 종료
/*
        if (mapView.isAutoRotateEnabled()) {   //지도 회전기능이 활성화 상태라면
            mMyLocationOverlay.setCompassHeadingVisible(false);  //나침반 각도표시 제거
            mMapCompassManager.disableCompass();  //나침반 모니터링 종료
            mapView.setAutoRotateEnabled(false, false);

         //    지도 회전기능 중지
        }
*/
        Log.e(TAG, "Naver stopMyLocation");
//        Toast.makeText(getActivity(), "Naver GPS 종료", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart() {
        super.onStart();

        mMapContext.onStart();
//        Log.e(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapContext.onResume();
        Log.e(TAG, "onResume()");
        Log.e(TAG, "onResume() start");
        if (StartFlag == true) {
            startMyLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapContext.onPause();
        Log.e(TAG, "onPause()");

    }

    @Override
    public void onStop() {

        mMapContext.onStop();

        super.onStop();
        Log.e(TAG, "onStop()");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.e(TAG, "onDestroyView()");


    }

    @Override
    public void onDestroy() {
        //     mMapContext.onDestroy();
        stopMyLocation();
        super.onDestroy();
//        Log.e(TAG, "onDestroy()");

    }


}