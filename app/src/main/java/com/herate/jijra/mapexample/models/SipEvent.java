package com.herate.jijra.mapexample.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by jijra on 17.7.2015.
 *
 * SipEvent class implementation. Self-explanatory, for the most part.
 */
public class SipEvent implements Comparable<SipEvent> {
    private static final String TAG = SipEvent.class.getSimpleName();
    private static final String JSON_NICKNAME = "nickname";
    private static final String JSON_DATE = "datetime";
    private static final String JSON_LATITUDE = "latitude";
    private static final String JSON_LONGITUDE = "longitude";

    private String mNickName;
    private Date mDate;
    private Date date;
    private String nick;
    private double latitude;
    private double longitude;
    private double mLatitude;
    private double mLongitude;


    public SipEvent(){
        Log.d(TAG, "Creating new SipEvent from empty");

    }

    public SipEvent(String nickName, Date date, double latitude, double longitude){
        Log.d(TAG, "Creating new SipEvent from java data");
        mDate = date;
        mNickName = nickName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public SipEvent(JSONObject json) throws JSONException{
        Log.d(TAG, "Creating new SipEvent from JSON");
        mNickName = json.getString(JSON_NICKNAME);
        mDate = new Date(json.getLong(JSON_DATE));
        mLatitude = Double.parseDouble(json.getString(JSON_LATITUDE));
        mLongitude = Double.parseDouble(json.getString(JSON_LONGITUDE));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_NICKNAME, mNickName);
        json.put(JSON_LATITUDE, mLatitude);
        json.put(JSON_LONGITUDE, mLongitude);
        return json;
    }

    public Date getDate(){
        return mDate;
    }

    public String getNick(){
        return mNickName;
    }

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }

    @Override
    public int compareTo(SipEvent o){
        if(getDate() == null || o.getDate() == null) return 0;
        return getDate().compareTo(o.getDate())*-1;
    }
}
