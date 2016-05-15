package com.example.namsoo.myslidingmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gc.materialdesign.views.ButtonRectangle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 김나영 on 2015-09-17.
 */
public class LaughterTherapyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laughtertherapy);

        ButtonRectangle btn_exit = (ButtonRectangle) findViewById(R.id.btn_exit);

        //비디오뷰 선언
        VideoView videoView = (VideoView)findViewById(R.id.video_laughtertherapy);

        //비디오뷰 커스텀 위해 미디어컨트롤러 객체 생성
        MediaController mediaController = new MediaController(this);

        //비디오뷰에 연결하기
        mediaController.setAnchorView(videoView);
        Uri video = Uri.parse("android.resource://"+getPackageName()+"/raw/songagi");

        videoView.setMediaController(mediaController);

        videoView.setVideoURI(video);

        videoView.requestFocus();

        videoView.start();


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                finish();
            }
        }
                , 31000);


        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
