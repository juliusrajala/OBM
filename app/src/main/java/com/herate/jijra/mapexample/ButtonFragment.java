package com.herate.jijra.mapexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jijra on 17.7.2015.
 */
public class ButtonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = new View(getActivity());//TODO: Replace with something sensible
        return v;
    }


    public static ButtonFragment newInstance(){
        return new ButtonFragment();
    }
}
