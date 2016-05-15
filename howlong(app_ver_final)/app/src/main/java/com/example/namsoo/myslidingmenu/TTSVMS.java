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
public class TTSVMS extends Service implements OnInitListener {
    private TextToSpeech mTts;

    @Override
    public void onCreate() {
        mTts = new TextToSpeech(this, this);
        mTts.setLanguage(Locale.KOREA);

        startService(new Intent("TTS_VMS"));

    }

    @Override
    public void onInit(int status) {


        //String myText = "빗길 사고 주의 비 오는 구간 절대감속";
        //mTts.speak(myText, TextToSpeech.QUEUE_ADD, null);
        mTts.speak(PopUpVMSActivity.vmsMessage_textview.getText().toString(), TextToSpeech.QUEUE_ADD, null);
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