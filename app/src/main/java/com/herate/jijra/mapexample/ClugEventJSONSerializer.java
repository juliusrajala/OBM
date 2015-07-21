package com.herate.jijra.mapexample;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by jijra on 17.7.2015.
 */
public class ClugEventJSONSerializer {
    private Context mContext;
    private String mFileName;

    public ClugEventJSONSerializer(Context c, String f){
        mContext = c;
        mFileName = f;
    }

    public ArrayList<SipEvent> loadEvents() throws IOException, JSONException{
        ArrayList<SipEvent> events = new ArrayList<>();
        BufferedReader reader = null;
        try{
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine())!= null){
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for(int i = 0; i<array.length();i++){
                events.add(new SipEvent(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            //Error thrown when no file
        }finally {
            if(reader != null){
                reader.close();
            }
        }
        return events;
    }

    public void saveEvents(ArrayList<SipEvent> events) throws JSONException, IOException{
        JSONArray array = new JSONArray();
        for(SipEvent c : events)
            array.put(c.toJSON());

        //Write to disk
        Writer writer = null;
        try{
            OutputStream out = mContext
                    .openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if(writer != null)
                writer.close();
        }
    }
}
