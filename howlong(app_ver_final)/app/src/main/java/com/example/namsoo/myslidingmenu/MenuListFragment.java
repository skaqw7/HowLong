package com.example.namsoo.myslidingmenu;

/**
 * Created by namsoo on 2015-08-09.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuListFragment extends ListFragment {

    View list_header;
    //ArrayList<SampleAdapter> sampleAdapterArrayList;

    public MenuListFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        SampleAdapter adapter = new SampleAdapter(getActivity());

        list_header = getLayoutInflater(savedInstanceState).inflate(R.layout.sliding_menu_header, null);



        adapter.add(new SampleItem("HowLong", R.drawable.icon_home));
        adapter.add(new SampleItem("교통소통정보", R.drawable.map_icon));
        adapter.add(new SampleItem("도로 전광판", R.drawable.display_icon));
        adapter.add(new SampleItem("토픽 뉴스", R.drawable.icon_news));
        adapter.add(new SampleItem("날씨", R.drawable.icon_cloud));
        adapter.add(new SampleItem("셋팅", R.drawable.setting_icon));

        setListAdapter(adapter);


    }




    public class SampleAdapter extends ArrayAdapter<SampleItem> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(getItem(position).iconRes);

            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(getItem(position).tag);

            return convertView;
        }
    }



    private class SampleItem {
        public String tag;
        public int iconRes;

        public SampleItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }







    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        switch (position) {
            case 0:
                ((BaseActivity) getActivity()).fragmentReplace(0);
                break;
            case 1:
                ((BaseActivity) getActivity()).fragmentReplace(1);
                break;

            case 2:
                ((BaseActivity) getActivity()).fragmentReplace(2);
                break;
            case 3:
                ((BaseActivity) getActivity()).fragmentReplace(3);
                break;
            case 4:
                ((BaseActivity) getActivity()).fragmentReplace(4);
                break;
            case 5:
                ((BaseActivity) getActivity()).fragmentReplace(5);
                break;
            case 6:
                ((BaseActivity) getActivity()).fragmentReplace(6);
                break;
        }
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {

        super.getListView().addHeaderView(list_header);
        super.setListAdapter(adapter);
    }

}