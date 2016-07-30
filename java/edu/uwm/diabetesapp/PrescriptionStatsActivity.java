package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PrescriptionStatsActivity extends Activity {

    PrescriptionDatabaseHelper2 prescriptiondb;
    ListView prescriptionlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_stats); //reuse?

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
                MedicationString = formatDatTime(entry.getNextOccurrence()) + "\n" + entry.getMedication() +
                        "\n" + formatFrequency(entry.getFrequency()) + " until \n" + formatDatTime(entry.getEventEnd());

                medicationScriptsArray.add(medicationIndex, MedicationString);

                dbList += "\n" + MedicationString;
            }
        }
        Log.v("MED RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, medicationScriptsArray);
        prescriptionlist = (ListView) findViewById(R.id.medicationstats_list);
        prescriptionlist.setAdapter(mArrayAdapter);
    }


    private String formatDatTime(long epoch){
        Date date = new Date(epoch*1000);  //set a date to epoch in milliseconds
        String dateTime =  new SimpleDateFormat("hh:mm aa EEE, MMM dd, yyyy").format(date);
        return dateTime;
    }

    private String formatFrequency(long freqMin){ //takes in frequency in minutes and returns a pretty string
        if (freqMin==0) return ("once");
        else if (freqMin<60) return ("every " + freqMin + " minutes");
        else if (freqMin==60) return("every hour");
        else if (freqMin<1440) return("every " + (freqMin/60) + " hours");
        else if (freqMin==1440) return("every day");
        else if (freqMin<10080) return("every " + (freqMin/1440) + " days");
        else if (freqMin==10080) return("every week");
        else return ("every " + freqMin + " minutes"); //TODO figure out month and year
    }
}
