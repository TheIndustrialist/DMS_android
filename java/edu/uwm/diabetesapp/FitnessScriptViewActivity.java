package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FitnessScriptViewActivity extends Activity {

    PrescriptionDatabaseHelper2 prescriptiondb;
    ListView prescriptionlist;
    AppHelpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_stats); //reuse?

        helper = new AppHelpers();
        prescriptiondb = PrescriptionDatabaseHelper2.getInstance(this); //return reference to master db

        ArrayList<PrescriptionEntry> allStatsArray = prescriptiondb.getAllItems();

        ArrayList<String> fitnessScriptsArray = new ArrayList<>();
        int fitnessIndex = 0;
        String FitnessString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just med prescription
        for (int i=0; i<prescriptiondb.getEntryCount(); i++){
            PrescriptionEntry entry = allStatsArray.get(i);
            if (entry.getCode()==2){//it's a fitness entry
                FitnessString = helper.formatDatTime(entry.getNextOccurrence()) + "\n" + entry.getExercise() +
                        "\n" + helper.formatFrequency(entry.getFrequency()) + " until \n" + helper.formatDatTime(entry.getEventEnd());

                fitnessScriptsArray.add(fitnessIndex, FitnessString);

                dbList += "\n" + FitnessString;
            }
        }
        Log.v("FITNESS RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, fitnessScriptsArray);
        prescriptionlist = (ListView) findViewById(R.id.exercisestats_list);
        prescriptionlist.setAdapter(mArrayAdapter);
    }



}
