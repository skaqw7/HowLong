package com.example.namsoo.myslidingmenu.tutorial;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.namsoo.myslidingmenu.MainActivity;
import com.example.namsoo.myslidingmenu.R;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ViewPagerStyle2Activity extends FragmentActivity {
    private ViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    private Button _btn1, _btn2, _btn3, _btn4;


    AlertDialog.Builder builder;
    int mode;

//    HomeActivity homeAct;


    public static final int TYPE_URI = 2;

    ImageView btn_go;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpView();
        setTab();

//        homeAct = new HomeActivity();

        Intent getinent = getIntent();

        mode = getinent.getIntExtra("mode", 0);

        btn_go = (ImageView) findViewById(R.id.btn_go);

        if (mode == 1) {
            btn_go.setBackgroundResource(R.drawable.right);

        } else if (mode == 2) {
            btn_go.setBackgroundResource(R.drawable.exit);
        }

        TextView textView = (TextView) findViewById(R.id.safecall);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "addi.ttf");
        textView.setTypeface(typeface);


        // NFC 관련 객체 생성
//        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode == 1) {
                    Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent2);
                    finish();
                } else if (mode == 2) {
                    finish();
                }
            }
        });

    }


    private void setUpView() {
        _mViewPager = (ViewPager) findViewById(R.id.viewPager);
        _adapter = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void setTab() {
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                btnAction(position);
            }

        });

    }


    private void btnAction(int action) {
        switch (action) {
            case 0: {


                _btn1.setBackgroundResource(R.drawable.rounded_cell_active);
                _btn2.setBackgroundResource(R.drawable.rounded_cell);
                _btn3.setBackgroundResource(R.drawable.rounded_cell);
                _btn4.setBackgroundResource(R.drawable.rounded_cell);

                break;
            }
            case 1: {

                _btn1.setBackgroundResource(R.drawable.rounded_cell);
                _btn2.setBackgroundResource(R.drawable.rounded_cell_active);
                _btn3.setBackgroundResource(R.drawable.rounded_cell);
                _btn4.setBackgroundResource(R.drawable.rounded_cell);
                break;
            }
            case 2: {

                _btn1.setBackgroundResource(R.drawable.rounded_cell);
                _btn2.setBackgroundResource(R.drawable.rounded_cell);
                _btn3.setBackgroundResource(R.drawable.rounded_cell_active);
                _btn4.setBackgroundResource(R.drawable.rounded_cell);
                break;
            }
            case 3: {

                _btn1.setBackgroundResource(R.drawable.rounded_cell);
                _btn2.setBackgroundResource(R.drawable.rounded_cell);
                _btn3.setBackgroundResource(R.drawable.rounded_cell);
                _btn4.setBackgroundResource(R.drawable.rounded_cell_active);
                break;
            }
        }
    }

    private void initButton() {
        _btn1 = (Button) findViewById(R.id.btn1);
        _btn2 = (Button) findViewById(R.id.btn2);
        _btn3 = (Button) findViewById(R.id.btn3);
        _btn4 = (Button) findViewById(R.id.btn4);


        _btn1.setBackgroundResource(R.drawable.rounded_cell_active);
        _btn2.setBackgroundResource(R.drawable.rounded_cell);
        _btn3.setBackgroundResource(R.drawable.rounded_cell);
        _btn4.setBackgroundResource(R.drawable.rounded_cell);

    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            finish();
        }
    }

    /**
     * Disable NFC adapter from read mode
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Enable NFC adapter to read mode
     */
    @Override
    protected void onResume() {
        super.onResume();

    }


}