package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MedicationStatsActivity extends Activity {

    DatabaseHelper diabeticdb;
    ListView medicationStatslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_stats);

        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db

        ArrayList<DiabeticEntry> allStatsArray = diabeticdb.getAllItems();

        ArrayList<String> medicationStatsArray = new ArrayList<>();
        int medicationIndex = 0;
        String MedicationString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just med stats
        for (int i=0; i<diabeticdb.getEntryCount(); i++){
            DiabeticEntry entry = allStatsArray.get(i);
            if (entry.getCode()==3){//it's a med entry
                MedicationString = entry.getTime() + "\n" + entry.getMedication();
                medicationStatsArray.add(medicationIndex, MedicationString);

                dbList += "\n" + MedicationString;
            }
        }
        Log.v("MED RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, medicationStatsArray);
        medicationStatslist = (ListView) findViewById(R.id.medicationstats_list);
        medicationStatslist.setAdapter(mArrayAdapter);

    }



}
