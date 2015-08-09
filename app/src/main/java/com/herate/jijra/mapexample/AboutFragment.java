package com.herate.jijra.mapexample;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jijra on 9.8.2015.
 */
public class AboutFragment extends Fragment{
    private TextView title;
    private TextView content;
    private TextView content2;
    private TextView content3;
    private LinearLayout background;
    private Typeface font;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_about, parent, false);
        title = (TextView)v.findViewById(R.id.title);
        content = (TextView)v.findViewById(R.id.content);
        content2 = (TextView)v.findViewById(R.id.content2);
        content3 = (TextView)v.findViewById(R.id.content3);
        background = (LinearLayout)v.findViewById(R.id.about_background);
        font = Typeface.createFromAsset(
                getActivity().getAssets(),
                "Roboto-Thin.ttf");
        title.setTypeface(font);
        content.setTypeface(font);
        content2.setTypeface(font);
        content3.setTypeface(font);

        return v;
    }

    public static AboutFragment newInstance(){
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}

