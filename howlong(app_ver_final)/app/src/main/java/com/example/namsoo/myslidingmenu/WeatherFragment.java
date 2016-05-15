package com.example.namsoo.myslidingmenu;

/**
 * Created by namsoo on 2015-08-09.
 */

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

public class WeatherFragment extends Fragment {

    WebView webView;


    ProgressBarCircularIndeterminate bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.webviewlayout, container, false);


        bar = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressBarCircularIndeterminate);


        webView = (WebView) v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://m.weather.naver.com/");
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebChromeClient(new GeoWebChromeClient());

        webView.setWebViewClient(new WebViewClient());
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //This is the filter
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;


                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        ((MainActivity) getActivity()).onBackPressed();
                    }

                    return true;
                }

                return false;
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            // 페이지 로딩 시작시 호출
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                bar.setVisibility(View.VISIBLE);
            }

            //페이지 로딩 종료시 호출
            public void onPageFinished(WebView view, String Url) {
                bar.setVisibility(View.GONE);
            }
        });

        return v;
    }


    public class GeoWebChromeClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }

    }
}