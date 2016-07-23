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
                NutritionString = formatTime(entry.getTime()) + "\n" + entry.getDiet();
                nutritionStatsArray.add(nutritionIndex, NutritionString);

                dbList += "\n" + NutritionString;
            }
        }
        Log.v("DIET RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, nutritionStatsArray);
        nutritionStatslist = (ListView) findViewById(R.id.nutritionstats_list);
        nutritionStatslist.setAdapter(mArrayAdapter);

    }

    //~~Helper for formatting the time to 12 hour clock~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private String formatTime (String dateTime){
        //String dateTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
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
