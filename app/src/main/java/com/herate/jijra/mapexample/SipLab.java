package com.herate.jijra.mapexample;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jijra on 17.7.2015.
 */
public class SipLab {
    private static final String TAG = SipLab.class.getSimpleName();
    private static final String FILENAME = "clugs.json";

    private ArrayList<SipEvent> mSipEvents;
    private ClugEventJSONSerializer mSerializer;

    private static SipLab sipLab;
    private Context mAppContext;

    private SipLab(Context appContext){
        mAppContext = appContext;
        mSerializer = new ClugEventJSONSerializer(mAppContext, FILENAME);

        try{
            mSipEvents = mSerializer.loadEvents();
        }catch (Exception e){
            mSipEvents = new ArrayList<>();
            Log.e(TAG, "Error loading scores ", e);
        }

    }

    public static SipLab get(Context c){
        if(sipLab == null){
            sipLab = new SipLab(c.getApplicationContext());
        }
        return sipLab;
    }

    public void addEvent(SipEvent sipEvent){
        mSipEvents.add(sipEvent);
    }

    public ArrayList<SipEvent> getSips(){ return mSipEvents; }

    public boolean saveSipEvents(){
        try {
            mSerializer.saveEvents(mSipEvents);
            Log.d(TAG, "Events saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG, "error saving events", e);
            return false;
        }
    }
}
