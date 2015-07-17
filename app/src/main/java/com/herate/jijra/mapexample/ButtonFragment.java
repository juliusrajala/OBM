package com.herate.jijra.mapexample;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jijra on 17.7.2015.
 *
 *
 **/
public class ButtonFragment extends Fragment implements FragmentLifecycle {
    private static final String TAG = ButtonFragment.class.getSimpleName();

    private FloatingActionButton mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_button, parent, false);

        mButton = (FloatingActionButton)v.findViewById(R.id.add_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hold button to register drink",Toast.LENGTH_SHORT).show();
            }
        });

        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "New marker added.", Toast.LENGTH_SHORT).show();
                ClugEvent clugEvent = new ClugEvent();
                ClugLab.get(getActivity()).addEvent(clugEvent);
                return false;
            }
        });

        return v;
    }

    public ClugEvent makeEvent(){
        ClugEvent clugEvent = new ClugEvent();
        //TODO: Make getters get actual information here
        return clugEvent;
    }

    @Override
    public void onPauseFragment(){
        Log.i(TAG, "onPauseFragment()");
    }

    @Override
    public void onResumeFragment(){
        Log.i(TAG, "onResumeFragment()");
    }


    public static ButtonFragment newInstance(){
        return new ButtonFragment();
    }
}
