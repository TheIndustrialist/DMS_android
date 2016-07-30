package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NutritionScriptViewActivity extends Activity {

    PrescriptionDatabaseHelper2 prescriptiondb;
    ListView prescriptionlist;
    AppHelpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_stats); //reuse?

        helper = new AppHelpers();
        prescriptiondb = PrescriptionDatabaseHelper2.getInstance(this); //return reference to master db

        ArrayList<PrescriptionEntry> allStatsArray = prescriptiondb.getAllItems();

        ArrayList<String> nutritionScriptsArray = new ArrayList<>();
        int nutritionIndex = 0;
        String NutritionString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just med prescription
        for (int i=0; i<prescriptiondb.getEntryCount(); i++){
            PrescriptionEntry entry = allStatsArray.get(i);
            if (entry.getCode()==1){//it's a nutrition entry
                NutritionString = helper.formatDatTime(entry.getNextOccurrence()) + "\n" + entry.getDiet() +
                        "\n" + helper.formatFrequency(entry.getFrequency()) + " until \n" + helper.formatDatTime(entry.getEventEnd());

                nutritionScriptsArray.add(nutritionIndex, NutritionString);

                dbList += "\n" + NutritionString;
            }
        }
        Log.v("DIET RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, nutritionScriptsArray);
        prescriptionlist = (ListView) findViewById(R.id.nutritionstats_list);
        prescriptionlist.setAdapter(mArrayAdapter);
    }


}
