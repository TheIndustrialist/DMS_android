package edu.uwm.diabetesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FitnessActivity extends Activity {

    private FitnessEvent fitnessItem;
    DatabaseHelper fitnessdb;
    private EditText fitnessTime;
    private EditText fitnessExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        fitnessItem = new FitnessEvent();
        fitnessdb = DatabaseHelper.getInstance(this); //return reference to master db

        //~~set up the exercise EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        fitnessExercise = (EditText) findViewById(R.id.FitnessExercise_txt);
        fitnessExercise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fitnessExercise.setText("");
            }
        });
        fitnessExercise.addTextChangedListener(fitnessExerciseTextWatcher);

        //~~set up the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        fitnessTime = (EditText) findViewById(R.id.FitnessTime_txt);
        fitnessTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
        //TODO set up broadcast listener so this updates??
        fitnessTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fitnessTime.setText("");
            }
        });
        fitnessTime.addTextChangedListener(fitnessTimeTextWatcher);

        //~~set up the save button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button FitnessSaveBtn = (Button) findViewById(R.id.FitnessSave_btn);
        FitnessSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveFitnessEvent();
            }
        });

        //~~set up the stats button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button FitnessStatsBtn = (Button) findViewById(R.id.FitnessStats_btn);
        FitnessStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFitnessStats = new Intent(FitnessActivity.this, FitnessStatsActivity.class);
                startActivity(gotoFitnessStats);
            }
        });

        //~~set up the prescription button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button FitnessScriptBtn = (Button) findViewById(R.id.FitnessScript_btn);
        FitnessScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the fitness prescription db
                //testing clearing the db
                fitnessdb.clearDatabase();
            }
        });
}
    //~~TextWatcher method for the exercise EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher fitnessExerciseTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
                fitnessItem.setExercise(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };


    //~~TextWatcher method for the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher fitnessTimeTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //TODO CATCH AN EXCEPTION WHEN THE INPUT IS NOT LEGIT
            //fitnessItem.setTime(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~ method for saving the fitness event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveFitnessEvent(){
         fitnessItem.setTime(fitnessTime.getText().toString()); //in case the user didn't change it
         boolean saved = fitnessdb.saveEvent(fitnessItem.getTime(), 2,0,null,fitnessItem.getExercise(),null);
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(FitnessActivity.this, "Exercise Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(FitnessActivity.this, "Error: Exercise Not Saved", Toast.LENGTH_LONG).show();
    }


}
