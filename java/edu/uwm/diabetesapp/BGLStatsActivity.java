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
                BGLString = entry.getTime() + "\n BGL: " + entry.getBGL() + "mg/dl";
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

}
