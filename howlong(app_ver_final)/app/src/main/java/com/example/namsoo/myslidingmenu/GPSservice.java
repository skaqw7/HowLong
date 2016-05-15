package com.example.namsoo.myslidingmenu;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;

public class GPSservice extends Service {

    String TAG = "GPSservice";

    Double X = 0.0;
    Double Y = 0.0;
    Integer id = 1001;
    int MAX_INDEX = 35;
    double MIN_SPEED = 40.00;
    double[] temp_distanceInMeters = new double[MAX_INDEX];

    static double sum_len = 0;
    int past_time = 0;
    boolean first_flag = true;

    //CharSequence currText_gps;
    String currValue_gps = "5";
    int GPS_reload_time = 0;


    private Handler mHandler;
    static private Location_pic mLocation_pic;


    /*  GPS Added Part */
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; //1초
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; //1미터
    private static final long MIN_DISTANCE_RADIUS_FOR_ALARM = 100;

    private boolean isGpsUse; //gps 사용가능한지 알려주는 Flag
    LocationManager locationManager;
    GPSListener gpsListener;
    public static Location myLoction = null;
    public static double getSpeed = 0; //근접경보 기능(브로드캐스트리시버)에 속도값에 접근하기위해서 static선언

    Double latitude = 0.0;
    Double longitude = 0.0;
    int temp_index = 0;
    public static double avg_time = 0.0;


    ArrayList<PendingIntent> pendingList = new ArrayList<PendingIntent>();
    ArrayList<PendingIntent> pendingList_VMS = new ArrayList<PendingIntent>();
    PendingIntent pIntent;
    PendingIntent pIntent_VMS;


    /*  GPS Added Part End */


    public DbManager db;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        startLocationService(); //StartService
        db = new DbManager(getApplicationContext(), 0);

        mHandler = new Handler();
        Log.e(TAG, "onCreate()");
        currValue_gps = PreferenceFragment.currValue;
    }

    public void startLocationService() {
        MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver(); //BroadcastReceiver 등록
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //Location 서비스 등록
        isGpsUse = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //GPS 사용 가능 여부
        gpsListener = new GPSListener(); //리스너 등록


        //GPS센서를 이용한 위치 요청
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                gpsListener);

        // 네트워크를 이용한 위치 요청
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                gpsListener);

        // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
        try {
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
            /*    latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                getSpeed = lastLocation.getSpeed();*/
                Log.e(TAG, "getLastKnownLocation");

                myLoction = lastLocation;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Toast.makeText(getActivity(), "위치 확인이 시작되었습니다.", Toast.LENGTH_SHORT).show();



        /*Handler Location_pick = new Handler();
        Location_pick.postDelayed(new Runnable() {

            @Override
            public void run() {

                //스타트로케이션
                //finish();       // 3 초후 이미지를 닫아버림

                pendingList.clear();
                PendingIntent pIntent = register(id++, db.x[temp_index], db.y[temp_index], MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
                if (pIntent != null) {
                    pendingList.add(pIntent);
                }
            }
        }, 300000);*/


        //학교용 테스트셋
//        37.54979600778847
//        127.07497451260438
        //랩실 테스트셋
//        37.55192534719128
//        127.07381384429014

    /*    pendingList.clear();
        PendingIntent pIntent = register(1001, 0, 0, MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
        if (pIntent != null) {
            pendingList.add(pIntent);
        }*/


        /* ------------------------------- 운동장 VDS ------------------------------- */

/*


        pendingList.clear();
        pIntent = register(1002,  37.6503227,127.1434925, MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
        if (pIntent != null) {
            pendingList.add(pIntent);
        }
*/

        /* ------------------------------- 운동장 VMS ------------------------------- */

/*
        pendingList_VMS.clear();
        pIntent_VMS = registe_vms(1001,     37.550884,127.074988, MIN_DISTANCE_RADIUS_FOR_ALARM, -1);
        if (pIntent_VMS != null) {
            pendingList_VMS.add(pIntent_VMS);
        }

*/


/*        pendingList_VMS.clear();
        pIntent_VMS = registe_vms(1001,     37.643647, 127.033172, MIN_DISTANCE_RADIUS_FOR_ALARM, -1);
        if (pIntent_VMS != null) {
            pendingList_VMS.add(pIntent_VMS);
        }*/
   /*     pendingList.clear();
        pIntent = register(1002, 37.612358, 127.142419, MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
        if (pIntent != null) {
            pendingList.add(pIntent);
        }

        pendingList.clear();
        pIntent = register(1003, 37.589515, 127.156406, MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
        if (pIntent != null) {
            pendingList.add(pIntent);
        }
*/

        /* ------------------------------- 근접경보  ------------------------------- */

        pendingList.clear();
        pIntent = register(1002, 37.55192534719128, 127.07381384429014, MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
        if (pIntent != null) {
            pendingList.add(pIntent);
        }
        /* ------------------------------- 근접경보  ------------------------------- */



        /* ------------------------------- add part ------------------------------- */

 /*       pendingList_VMS.clear();
        pIntent_VMS = registe_vms(1001, 37.55192534719128, 127.07381384429014, MIN_DISTANCE_RADIUS_FOR_ALARM, -1);
        if (pIntent_VMS != null) {
            pendingList_VMS.add(pIntent_VMS);
        }
*/
        /* ------------------------------- add part ------------------------------- */


        mLocation_pic = new Location_pic(true);
        mLocation_pic.start();

        Log.e(TAG, "startLocationService");
    }

    /*
    * 쓰레드 시작 //////////////////////////////////////////////////////////////////////////////
    */
    class Location_pic extends Thread {
        private boolean isPlay = false;

        public Location_pic(boolean isPlay) {
            this.isPlay = isPlay;
        }

        public void stopThread() {
            isPlay = !isPlay;
        }

        @Override
        public void run() {
            super.run();
            while (isPlay) {
                try {
                    if (currValue_gps == "5") {
                        GPS_reload_time = 300000;
                    } else if (currValue_gps == "15") {
                        GPS_reload_time = 900000;
                    } else if (currValue_gps == "30") {
                        GPS_reload_time = 1800000;
                    } else if (currValue_gps == null) {
                        GPS_reload_time = 300000;
                    } else {
                        GPS_reload_time = 300000;
                    }
                    Thread.sleep(GPS_reload_time); // 5분300000
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (pendingList.size() != 0) {
                            unregister();
                        }
                        pendingList.clear();



                        /* ------------------------------- add part ------------------------------- */







                        /* ------------------------------- add part ------------------------------- */


                        PendingIntent pIntent = register(id++, db.x[temp_index], db.y[temp_index], MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
                        if (pIntent != null) {
                            pendingList.add(pIntent);
                        }
                        Log.e(TAG, "위치를 저장 한다 한다 한다아아앙");
                    }
                });
            }
        }
    }

    /*
    * 쓰레드 종료 //////////////////////////////////////////////////////////////////////////////
    */

    /*       GPS 서비스 등록 해제       */
    private PendingIntent register(int id, double latitude, double longitude, float radius, long expiration) {
        Intent proximityIntent = new Intent("VDS_BroadcastReceiver");
        proximityIntent.putExtra("id", id);
        proximityIntent.putExtra("latitude", latitude);
        proximityIntent.putExtra("longitude", longitude);
        proximityIntent.putExtra("speed", getSpeed);
        // 대기하고있다가 어떤 조건에의해서 시스템쪽에서 실행 되는 것
        proximityIntent.putExtra("timevalue", avg_time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, proximityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 그 위치에 근접했을때 인텐트를 날릴 것이다.
        locationManager.addProximityAlert(latitude, longitude, radius, expiration, pendingIntent);
        //Toast.makeText(getActivity(), "근접 경보 등록", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "register()");

        return pendingIntent;
    }

    private void unregister() {
        for (int i = 0; i < pendingList.size(); i++) {
            PendingIntent pIntent = pendingList.get(i);
            locationManager.removeProximityAlert(pIntent);
        }
        Log.e(TAG, "unregister()");

    }


    /*       GPS 서비스 VMS 등록 해제       */

    private PendingIntent registe_vms(int id, double latitude, double longitude, float radius, long expiration) {
        Intent proximityIntent = new Intent("VMS_BroadcastReceiver");
        proximityIntent.putExtra("id", id);
        proximityIntent.putExtra("latitude", latitude);
        proximityIntent.putExtra("longitude", longitude);
        proximityIntent.putExtra("speed", getSpeed);
        // 대기하고있다가 어떤 조건에의해서 시스템쪽에서 실행 되는 것
        proximityIntent.putExtra("timevalue", avg_time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, proximityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 그 위치에 근접했을때 인텐트를 날릴 것이다.
        locationManager.addProximityAlert(latitude, longitude, radius, expiration, pendingIntent);
        //Toast.makeText(getActivity(), "근접 경보 등록", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "register()");

        return pendingIntent;
    }

    private void unregister_vms() {
        for (int i = 0; i < pendingList.size(); i++) {
            PendingIntent pIntent = pendingList.get(i);
            locationManager.removeProximityAlert(pIntent);
        }
        Log.e(TAG, "unregister()");

    }


    private class GPSListener implements LocationListener {

        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            getSpeed = (location.getSpeed()) * 3.6;
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.i("GPSListener", " [ " + latitude + ", " + longitude + " ]");
            myLoction = location;

            db.synchronization();

            //temp_distanceInMeters = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
            temp_index = 0;
            id = 1001;

            for (int i = 0; i < MAX_INDEX; i++) {
                temp_distanceInMeters[i] = -1;
            }

            for (int i = 0; i < MAX_INDEX; i++) {
                if (db.speed[i] != -1 && db.speed[i] <= MIN_SPEED) {
                    //내 현재위치
                    Location loc1 = new Location("");
                    loc1.setLatitude(latitude);
                    loc1.setLongitude(longitude);

                    //비교할 cctv위치
                    Location loc2 = new Location("");
                    loc2.setLatitude(db.x[i]);
                    loc2.setLongitude(db.y[i]);

                    //1. 비교할 cctv가져와서 내 현재위치랑 거리 계산
                    double distanceInMeters = loc1.distanceTo(loc2);

                    temp_distanceInMeters[i] = distanceInMeters;
                    //Log.d("xy", "distanceInMeters : "+distanceInMeters);
                }
            }
//            for (int i = 0; i < MAX_INDEX; i++) {
//                Log.e("xy",i+"번 "+ String.valueOf(temp_distanceInMeters[i]));
//            }

            double Min = 100000000000.0;
            for (int i = 0; i < MAX_INDEX; i++) {
                if (temp_distanceInMeters[i] != -1) {
                    if (temp_distanceInMeters[i] < Min) {
                        Min = temp_distanceInMeters[i];
                        temp_index = i;
                    }
                }
            }
/*
            PendingIntent pIntent = register(id++, db.x[temp_index], db.y[temp_index], MIN_DISTANCE_RADIUS_FOR_ALARM, -1); //-1 이면 expiration은 적용 안된다.
            if (pIntent != null) {
                pendingList.add(pIntent);
            }

*/
            avg_time = get_avg_time();

            Log.e(TAG, "X: " + db.x[temp_index] + " Y: " + db.y[temp_index] + " Time: " + avg_time);
        }//**************************************************************************************************

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public double get_avg_time() {
        int start_index = temp_index;
        int end_index = 0;
        double sum_speed = 0;
        double sum_len2 = 0;

        for (int i = start_index; i >= 0; i--) {
            if (temp_distanceInMeters[i] == -1.0) {
                end_index = i + 1;
                break;
            }
        }

        Log.d("xy", "start_index : " + start_index + " end_index : " + end_index);

        if (start_index == end_index) {
            Log.d("xy", "start_index == end_index" + " avg_time : " + db.length[start_index] / db.speed[start_index] / 1000.0 * 60.0);
            sum_len = db.length[start_index];
            return db.length[start_index] / db.speed[start_index] / 1000.0 * 60.0;
        } else {
            for (int i = start_index; i >= end_index; i--) {


                sum_len2 = sum_len2 + db.length[i];

                sum_speed = sum_speed + db.speed[i];
            }
            sum_len = sum_len2;
            Log.d("xy", "sum speed : " + sum_speed);
            Log.d("xy", "sum len : " + sum_len2);
            Log.d("xy", "start_index != end_index" + " avg_time : " + sum_len2 / (sum_speed / (start_index - end_index + 1.0)) / 1000.0 * 60.0);
            return sum_len2 / (sum_speed / (start_index - end_index + 1.0)) / 1000.0 * 60.0;
        }
    }

    public void stopLocationService() {
        locationManager.removeUpdates(gpsListener);
        unregister();//근접경보해제

/* ------------------------------- add part ------------------------------- */


        unregister_vms();


/* ------------------------------- add part ------------------------------- */


        mLocation_pic.stopThread();// 5분 쓰레드 종료하기
        Log.e(TAG, "stopLocationService");
    }

    @Override
    public void onDestroy() {
        stopLocationService();
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
