package com.example.namsoo.myslidingmenu.tutorial;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.namsoo.myslidingmenu.R;


public class LayoutThree extends Fragment {

    public static Fragment newInstance(Context context) {
        LayoutThree f = new LayoutThree();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.layout_3, null);

        return root;
    }

}