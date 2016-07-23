package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FitnessStatsActivity extends Activity {

    DatabaseHelper diabeticdb;
    ListView fitnessStatslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_stats);

        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db

        ArrayList<DiabeticEntry> allStatsArray = diabeticdb.getAllItems();

        ArrayList<String> exerciseStatsArray = new ArrayList<>();
        int exerciseIndex = 0;
        String ExerciseString;
        String dbList = "\n"; //for the log window

        //Populate the array list of just exercise stats
        for (int i=0; i<diabeticdb.getEntryCount(); i++){
            DiabeticEntry entry = allStatsArray.get(i);
            if (entry.getCode()==2){//it's a fitness entry
                ExerciseString = formatTime(entry.getTime()) + "\n" + entry.getExercise();
                exerciseStatsArray.add(exerciseIndex, ExerciseString);

                dbList += "\n" + ExerciseString;
            }
        }
        Log.v("FITNESS RECORDS", dbList);

        ArrayAdapter mArrayAdapter = new ArrayAdapter<>(this,R.layout.list_item, R.id.list_item_textView, exerciseStatsArray);
        fitnessStatslist = (ListView) findViewById(R.id.exercisestats_list);
        fitnessStatslist.setAdapter(mArrayAdapter);
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
