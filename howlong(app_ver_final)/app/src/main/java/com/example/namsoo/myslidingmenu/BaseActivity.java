package com.example.namsoo.myslidingmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.namsoo.myslidingmenu.tutorial.ViewPagerStyle2Activity;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

    static SlidingMenu sm;
    /*  PreferenceActivity Added Part  */
    SharedPreferences sharedPrefs;
    static boolean notifications_sound;
    static boolean notifications_vibration;
    boolean tutorial_check;
    private static final int TUTORIAL_REQUESTQODE = 1000;
    Intent tutoIntent;
    ProgressBarCircularIndeterminate progressBarCircularIndeterminate;
    SharedPreferences sharedPrefsForClick;
//    static ListPreference listPreference;
//    static CharSequence currText;
//    static String currValue;
    /*  PreferenceActivity Added Part  */

    /*     Setting Flag save       */
    SharedPreferences prefsBase;

    SharedPreferences.Editor editorBase;
    /*     Setting Flag save       */

    protected ListFragment mFrag;
    String TAG = "BaseActivity";

    public BaseActivity() {
    }

    Fragment newFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        prefsBase = getSharedPreferences("set", 0);
        tutorial_check = prefsBase.getBoolean("tutorial_chk", false);
        notifications_sound = prefsBase.getBoolean("notifications_sound", true);
        notifications_vibration = prefsBase.getBoolean("notifications_vibration", true);
        Log.d(TAG, "onCreateBae > sound: " + notifications_sound + " / vibration: " + notifications_vibration);


        // set the Behind View
//        notifications_sound = sharedPrefsForClick.getBoolean("notifications_sound", false);
//        notifications_vibration = sharedPrefsForClick.getBoolean("notifications_vibration", false);

        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new MenuListFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (ListFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//
//
//        if (tutorial_check == false) {
//            startActivityForResult(tutoIntent, TUTORIAL_REQUESTQODE);
//            overridePendingTransition(R.anim.slide_up, 0);
//
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TUTORIAL_REQUESTQODE) {
            if (resultCode == RESULT_OK) {
                tutorial_check = true; //true로 바꿔주면 다시 안나올 것이다.
                editorBase = prefsBase.edit();
                editorBase.putBoolean("tutorial_chk", tutorial_check);
                editorBase.commit();
                Log.e(TAG, "RESULT_OK");
            } else if (resultCode == RESULT_CANCELED) {
                Log.e(TAG, "RESULT_CANCELED");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Log.e(TAG, "fragmentReplace");
        Fragment newFragment = null;
        newFragment = getFragment(reqNewFragmentIndex);

        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.fragment_mainContainer, newFragment);

        getSlidingMenu().showContent();
        transaction.commit();
    }

    private Fragment getFragment(int idx) {


        switch (idx) {
            case 1:
                newFragment = IntroFragment.newInstance(true);
                Log.d(TAG, "onResume > sound: " + notifications_sound + " / vibration: " + notifications_vibration);
                getActionBar().setTitle("HowLong");
                break;
            case 2:
                newFragment = UserMapFragment.newInstance(37.54979600778847, 127.07497451260438, 0.0);
                getActionBar().setTitle("교통소통정보");
                break;
            case 3:
                newFragment = new VMSFragment();
                getActionBar().setTitle("도로 전광판");
                break;
            case 4:
                progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
                getActionBar().setTitle("토픽 뉴스");
                newFragment = new NewsFragment();
                break;
            case 5:
                newFragment = new WeatherFragment();
                getActionBar().setTitle("날씨");
                break;
            case 6:
                getActionBar().setTitle("설정");
                newFragment = new PreferenceFragment() {

                    /*     Setting Flag save       */
                    SharedPreferences prefs;
                    SharedPreferences.Editor editor;
                    /*     Setting Flag save       */

                    @Override
                    public void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        addPreferencesFromResource(R.xml.preferences);
                    }

                    @Override
                    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
                        sharedPrefsForClick = PreferenceManager
                                .getDefaultSharedPreferences(getApplicationContext());


                        if (preference instanceof SwitchPreference) {
                            Toast.makeText(getApplicationContext(), "System ON", Toast.LENGTH_LONG).show();
                        }
                        if (preference instanceof CheckBoxPreference) {
                            notifications_sound = sharedPrefsForClick.getBoolean("notifications_sound", true);
                            notifications_vibration = sharedPrefsForClick.getBoolean("notifications_vibration", true);
                            Log.d(TAG, "onPreferenceTreeClick > sound: " + notifications_sound + " / vibration: " + notifications_vibration);

                            if (notifications_sound == true) {
//                                Toast.makeText(getApplicationContext(), "sound", Toast.LENGTH_LONG).show();
                            }
                            if (notifications_vibration == true) {
//                                Toast.makeText(getApplicationContext(), "vibration", Toast.LENGTH_LONG).show();
                            }
                        }
//                        if (preference instanceof ListPreference){
//
//                            if (preference.getKey().equals("sync_frequency")) {
//                                listPreference = (ListPreference) findPreference("sync_frequency");
//                                currText = listPreference.getEntry();
//                                currValue = listPreference.getValue();
//                                Log.e(TAG,"값이 저장 된다");
//                            }
//                        }

                        if (preference instanceof Preference) {
                            if (preference.getKey().equals("info_tutorial")) {
                                Intent infointent = new Intent(getApplicationContext(), ViewPagerStyle2Activity.class);
                                infointent.putExtra("mode", 2);
                                startActivity(infointent);
                                overridePendingTransition(R.anim.slide_up, 0);

                            }

                            if (preference.getKey().equals("info_made")) {
                                startActivity(new Intent(getApplicationContext(), MadeByActivity.class));
                            }
                        }
                        return super.onPreferenceTreeClick(preferenceScreen, preference);
                    }

                    @Override
                    public void onPause() {
                        super.onPause();
                        Log.d(TAG, "PreferenceFragment : onPause()");
                        editor = prefs.edit();
                        editor.putBoolean("notifications_sound", notifications_sound);
                        editor.putBoolean("notifications_vibration", notifications_vibration);
                        editor.commit();
                        Log.d(TAG, "onPause > sound: " + notifications_sound + " / vibration: " + notifications_vibration);

                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        Log.d(TAG, "PreferenceFragment : onStop()");


                    }

                    @Override
                    public void onDestroy() {
                        Log.d(TAG, "PreferenceFragment : onDestroy()");
                        super.onDestroy();
                    }

                    @Override
                    public void onResume() {
                        super.onResume();

                        Log.d(TAG, "PreferenceFragment: onResume()");
                        prefs = getSharedPreferences("set", 0);

                        notifications_sound = prefs.getBoolean("notifications_sound", true);
                        notifications_vibration = prefs.getBoolean("notifications_vibration", true);

                        Log.d(TAG, "onResume > sound: " + notifications_sound + " / vibration: " + notifications_vibration);


                    }
                };
                break;
            default:
                break;
        }
        return newFragment;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onUserLeaveHint() {
//여기서 감지
        Log.d(TAG, "Home Button Touch");
        super.onUserLeaveHint();
    }
}



