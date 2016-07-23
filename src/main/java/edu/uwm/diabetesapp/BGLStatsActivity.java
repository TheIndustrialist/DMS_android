package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BGLStatsActivity extends Activity {

    DatabaseHelper diabeticdb;
    ListView bglStatslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgl_stats);

        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db

        ArrayList<DiabeticEntry> allStatsArray = diabeticdb.getAllItems();

        ArrayList<String> bglStatsArray = new ArrayList<>();
        int bglIndex = 0;
        String BGLString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just BGL stats
        for (int i=0; i<diabeticdb.getEntryCount(); i++){
            DiabeticEntry entry = allStatsArray.get(i);
            if (entry.getCode()==0){//it's a BGL entry
                BGLString = formatTime(entry.getTime()) + "\n BGL: " + entry.getBGL() + "mg/dl";
                //BGLString = entry.getTime() + "\n BGL: " + entry.getBGL() + "mg/dl";
                bglStatsArray.add(bglIndex, BGLString);

                dbList += "\n" + BGLString;
            }
        }
        Log.v("BGL RECORDS", dbList);
        //OK, now send the bglStatsArray to the listview!!!

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, bglStatsArray);
        bglStatslist = (ListView) findViewById(R.id.bglstats_list);
        bglStatslist.setAdapter(mArrayAdapter);

    } //end OnCreate


    private String formatTime (String dateTime){
        String date = dateTime.substring(0,dateTime.indexOf(' '));
        String hour = dateTime.substring(dateTime.indexOf(' ')+1,dateTime.indexOf(':'));
        String min = dateTime.substring(dateTime.indexOf(':')+1);

        if (Integer.parseInt(hour)<12) {
            if (Integer.parseInt(hour)==0) {
                hour = Integer.toString(Integer.parseInt(hour)+12);//midnight to 12:59AM
            }
            return date + "   " + hour + ":" + min + "AM";

        }  else {
            if (Integer.parseInt(hour)>12) {
                hour = Integer.toString(Integer.parseInt(hour)-12);//1:00PM to 11:59PM
            } //else noon to 12:59PM
            return date + "   " + hour + ":" + min + "PM";
        }
    }







}
