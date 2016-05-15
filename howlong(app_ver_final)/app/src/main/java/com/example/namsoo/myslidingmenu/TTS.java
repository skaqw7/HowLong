package com.example.namsoo.myslidingmenu;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.Locale;

/**
 * Created by SangWook on 2015-07-24.
 */
public class TTS extends Service implements OnInitListener {
    private TextToSpeech mTts;

    @Override
    public void onCreate() {
        mTts = new TextToSpeech(this, this);
        mTts.setLanguage(Locale.KOREA);

        startService(new Intent("service.univ.sejong.locationmanager"));

    }

    @Override
    public void onInit(int status) {

        String temp = GPSservice.avg_time + "";

        if (temp.equals("NaN")) {
            String myText1 = "서울외곽순환고속도로";
            String myText2 = "전구간 소통 원활합니다.";
            mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
            mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);

        } else {
            String myText1 = "정체구간에 진입합니다 예상 소요시간은 약 ";
            String myText2 = ""+  String.format("%.0f", GPSservice.avg_time);
            String myText3 = "분 입니다.";
            mTts.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
            mTts.speak(myText2, TextToSpeech.QUEUE_ADD, null);
            mTts.speak(myText3, TextToSpeech.QUEUE_ADD, null);
        }





    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}