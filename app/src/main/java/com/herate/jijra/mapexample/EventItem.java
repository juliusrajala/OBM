package com.herate.jijra.mapexample;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by jijra on 7.8.2015.
 */
public class EventItem implements ClusterItem {
    private SipEvent event;
    private final LatLng position;
    public final String nick;
    public final int imageResource;

    public EventItem(SipEvent e){
        this.event = e;
        this.position = new LatLng(e.getLatitude(), e.getLongitude());
        this.imageResource = R.drawable.icon;
        this.nick = e.getNick();
    }

    @Override
    public LatLng getPosition(){
        return position;
    }
}
