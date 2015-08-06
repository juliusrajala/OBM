package com.herate.jijra.mapexample.models;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.herate.jijra.mapexample.R;

import java.util.List;

/**
 * Created by jijra on 5.8.2015.
 */
public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.ViewHolder>{

    //Declare variable to identify which view that is being inflated
    //The options are either the Navigation Drawer HeaderView or the list items in the Navigation drawer
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    // String Array to store the passed titles Value from MainActivity.java
    private String mNavTitles[];

    // Int Array to store the passed icons resource value from MainActivity.java
    private int mIcons[];

    //String Resource for header View Name
    private String name;

    //int Resource for header view profile picture
    private int profile;

    //String for the email displayed in the Navigation header

    private Context mContext;

    public Typeface font;


    /***
     *
     * @param parent
     * @param viewType - tells us what type of view that needs to be created, if this is call to create a Header view
     *                 we inflate header.xml and if this is call to for the navigation list view, we inflate the
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_bar_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(NavDrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            if(font != null)
                holder.textView.setTypeface(font);
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
        }
        else{

            holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            holder.Name.setText(name);
            if(font != null)
                holder.Name.setTypeface(font);
        }

    }

    /**
     *
     * @return returns the number of items present in the Navigation list
     */
    @Override
    public int getItemCount() {
        // the number of items in the list will be +1 the titles including the header view.
        return mNavTitles.length + 1;
    }

    /**
     * With this method we determine what type of view being passed.
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;

        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from nav_bar_rowrow.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from nav_bar_row.xmlxml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{


                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name

                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

    }


    public NavDrawerAdapter(List<DrawerItem> dataList, Context context, String Name, int Profile, Typeface font){

        this.font = font;
        mNavTitles = new String[dataList.size()];
        mIcons = new int[dataList.size()];

        for (int i = 0; i < dataList.size(); i++){
            mNavTitles[i] = dataList.get(i).getItemName();
            mIcons[i] = dataList.get(i).getImgResID();
        }
        mContext = context;
        name = Name;
        profile = Profile;                     //here we assign those passed values to the values we declared here
        //in adapter
    }



}