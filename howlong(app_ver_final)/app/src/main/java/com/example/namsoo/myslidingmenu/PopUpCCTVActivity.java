package com.example.namsoo.myslidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.Vector;


public class PopUpCCTVActivity extends Activity {
    public String VIDEO_URL = "";//http://cctvsec.ktict.co.kr/2154/5BKK1kppTDLZbZu832UFUoLK8Rq6m45sEguKpqnctfaYgxf733RdyiqRWtVhIz1s";
    VideoView videoView;


//    http://openapi.its.go.kr/api/NCCTVInfo?key=1436871309968&ReqType=2&MinX=127.156460&MaxX=127.165725&MinY=37.572916&MaxY=37.590178&type=ex

    // 구리tg
    // 37.590178, 127.156460
    //강일IC
    //37.572916, 127.165725
    //url띄워주기위해서
    CCTVParsing cctvParsing = new CCTVParsing();
    Vector<String> urlvec = new Vector<String>();
    Intent getIntent;
    ProgressBarCircularIndeterminate bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/* blur */
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 1f;
        getWindow().setAttributes(layoutParams);

       /* blur */

        setContentView(R.layout.activity_pop_up_cctv);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //투명배경

        ButtonRectangle btn_finish = (ButtonRectangle) findViewById(R.id.button);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bar = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        bar.setVisibility(View.VISIBLE);




        //url구분위해 클릭한 마커의 title받기
        getIntent = getIntent();
        // String cctvTitle = intent.getExtras().getString("title");
        VIDEO_URL = getIntent.getExtras().getString("url");

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(VIDEO_URL));
        // 미디어컨트롤러 추가하는부분
      //  MediaController controller = new MediaController(this);
      //  videoView.setMediaController(controller);

        // 준비하는 과정을 미리함
        videoView.requestFocus();

        // 동영상이 재생준비가 완료되엇을떄를 알수있는 리스너 (실제 웹에서 영상을 다운받아 출력할때 많이 사용됨)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            // 동영상 재생준비가 완료된후 호출되는 메서드
            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                /*Toast.makeText(getApplicationContext(),
                        "동영상이 준비되었습니다.\n'재생' 버튼을 누르세요.", Toast.LENGTH_LONG)
                        .show();*/
                bar.setVisibility(View.GONE);

                playVideo();

            }
        });

        // 동영상 재생이 완료된걸 알수있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            // 동영상 재생이 완료된후 호출되는 메서드
            public void onCompletion(MediaPlayer player) {
              /*  Toast.makeText(getApplicationContext(), "동영상 재생이 완료되었습니다.",
                        Toast.LENGTH_LONG).show();*/
            }
        });




    }

    private void playVideo() {
        // 비디오를 처음부터 재생할땐 0
        videoView.seekTo(0);
        // 비디오 재생 시작
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServiceMethod(this);
    }

    public void stopServiceMethod(Context context) {
        Intent Service = new Intent(context, TTS.class);
        context.stopService(Service);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pop_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
