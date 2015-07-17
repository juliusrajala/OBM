package com.herate.jijra.mapexample;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jijra on 17.7.2015.
 */
public class ClugLab {
    private static final String TAG = ClugLab.class.getSimpleName();
    private static final String FILENAME = "clugs.json";

    private ArrayList<ClugEvent> mClugEvents;
    private ClugEventJSONSerializer mSerializer;

    private static ClugLab clugLab;
    private Context mAppContext;

    private ClugLab(Context appContext){
        mAppContext = appContext;
        mSerializer = new ClugEventJSONSerializer(mAppContext, FILENAME);

        try{
            mClugEvents = mSerializer.loadEvents();
        }catch (Exception e){
            mClugEvents = new ArrayList<>();
            Log.e(TAG, "Error loading scores ", e);
        }

    }

    public static ClugLab get(Context c){
        if(clugLab == null){
            clugLab = new ClugLab(c.getApplicationContext());
        }
        return clugLab;
    }

    public void addEvent(ClugEvent clugEvent){
        mClugEvents.add(clugEvent);
    }

    public ArrayList<ClugEvent> getClugs(){ return mClugEvents; }

    public boolean saveClugEvents(){
        try {
            mSerializer.saveEvents(mClugEvents);
            Log.d(TAG, "Events saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG, "error saving events", e);
            return false;
        }
    }
}
