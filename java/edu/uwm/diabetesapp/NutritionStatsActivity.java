package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NutritionStatsActivity extends Activity {

    DatabaseHelper diabeticdb;
    ListView nutritionStatslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_stats); //change this once db is created

        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db

        ArrayList<DiabeticEntry> allStatsArray = diabeticdb.getAllItems();

        ArrayList<String> nutritionStatsArray = new ArrayList<>();
        int nutritionIndex = 0;
        String NutritionString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just diet stats
        for (int i=0; i<diabeticdb.getEntryCount(); i++){
            DiabeticEntry entry = allStatsArray.get(i);
            if (entry.getCode()==1){//it's a diet entry
                NutritionString = entry.getTime() + "\n" + entry.getDiet();
                nutritionStatsArray.add(nutritionIndex, NutritionString);

                dbList += "\n" + NutritionString;
            }
        }
        Log.v("DIET RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, nutritionStatsArray);
        nutritionStatslist = (ListView) findViewById(R.id.nutritionstats_list);
        nutritionStatslist.setAdapter(mArrayAdapter);

    }


}
