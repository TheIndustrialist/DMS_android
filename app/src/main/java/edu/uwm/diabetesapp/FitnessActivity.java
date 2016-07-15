package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class FitnessActivity extends Activity {

    DatabaseHelper fitnessdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        fitnessdb = DatabaseHelper.getInstance(this); //retrun reference to master db

        //TODO change the top button into a textviews for entering exercises and quantities


        Button FitnessSaveBtn = (Button) findViewById(R.id.FitnessSave_btn);
        FitnessSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Save the fitness to the fitness db
                boolean saved = fitnessdb.saveEvent(2,0,"","run","");
                //int code, int bgl, String diet, String exercise, String medication
                if( saved == true )
                    Toast.makeText(FitnessActivity.this, "Exercise Saved", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(FitnessActivity.this, "Error: Exercise Not Saved", Toast.LENGTH_LONG).show();
            }
        });

        Button FitnessStatsBtn = (Button) findViewById(R.id.FitnessStats_btn);
        FitnessStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fire off the activity to do something instead
                //testing
                String dbList = "\n";
                ArrayList<DiabeticEntry> itemList = fitnessdb.getAllItems();

                for (int i=0; i<fitnessdb.getEntryCount(); i++){
                    DiabeticEntry entry = itemList.get(i);
                    dbList += "\n" + entry.getTime() +
                            "\t" + entry.getCode() +
                            "\t" + entry.getBGL() +
                            "\t" + entry.getDiet() +
                            "\t" + entry.getExercise() +
                            "\t" + entry.getMedication();
                }

                Log.v("DATABASE RECORDS", dbList);
            }
        });

        Button FitnessScriptBtn = (Button) findViewById(R.id.FitnessScript_btn);
        FitnessScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the fitness prescription db
            }
        });






    }
}
