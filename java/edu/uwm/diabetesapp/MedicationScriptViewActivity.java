package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MedicationScriptViewActivity extends Activity {

    PrescriptionDatabaseHelper2 prescriptiondb;
    ListView prescriptionlist;
    AppHelpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_stats); //reuse?

        helper = new AppHelpers();
        prescriptiondb = PrescriptionDatabaseHelper2.getInstance(this); //return reference to master db

        ArrayList<PrescriptionEntry> allStatsArray = prescriptiondb.getAllItems();

        ArrayList<String> medicationScriptsArray = new ArrayList<>();
        int medicationIndex = 0;
        String MedicationString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just med prescription
        for (int i=0; i<prescriptiondb.getEntryCount(); i++){
            PrescriptionEntry entry = allStatsArray.get(i);
            if (entry.getCode()==3){//it's a med entry
                MedicationString = helper.formatDatTime(entry.getNextOccurrence()) + "\n" + entry.getMedication() +
                        "\n" + helper.formatFrequency(entry.getFrequency()) + " until \n" + helper.formatDatTime(entry.getEventEnd());

                medicationScriptsArray.add(medicationIndex, MedicationString);

                dbList += "\n" + MedicationString;
            }
        }
        Log.v("MED RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, medicationScriptsArray);
        prescriptionlist = (ListView) findViewById(R.id.medicationstats_list);
        prescriptionlist.setAdapter(mArrayAdapter);
    }



}
