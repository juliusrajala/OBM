package com.herate.jijra.mapexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jijra on 13.8.2015.
 */
public class AuthenticationFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_authentication, parent, false);

        return v;
    }


    public static AuthenticationFragment newInstance(){
        AuthenticationFragment fragment = new AuthenticationFragment();

        return fragment;
    }
}
