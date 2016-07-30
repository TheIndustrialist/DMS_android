package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
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


public class FitnessActivity extends Activity
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private FitnessEvent fitnessItem;
    DatabaseHelper fitnessdb;
    private EditText fitnessTime;
    private EditText fitnessExercise;
    private Button DateBtn;
    private Button TimeBtn;
    private AppHelpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        helper = new AppHelpers();
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

        //~~set up the to Date button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        DateBtn = (Button) findViewById(R.id.date_btn);
        DateBtn.setText(helper.getDate()); //init with current date
        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        //~~set up the time from button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        TimeBtn = (Button) findViewById(R.id.time_btn);
        TimeBtn.setText(helper.getTime()); //init with current time
        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

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
                Intent gotoFitnessScripts = new Intent(FitnessActivity.this, FitnessScriptActivity.class);
                startActivity(gotoFitnessScripts);
            }
        });

        fitnessItem.setEventDateTime(Calendar.getInstance());
    }//end OnCreate

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


    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        TimeBtn.setText(helper.formatTime(hour,minute));
        fitnessItem.setEventDateTime(hour,minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        DateBtn.setText(helper.formatDate(year,month+1,day));
        fitnessItem.setEventDateTime(year,month,day);

    }

    //~~method to launch the datepicker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void showDatePickerDialog(View v) {
        DialogFragment newDateFragment = new DatePickerFragment();
        newDateFragment.show(getFragmentManager(), "datePicker");
    }

    //~~method to launch the time picker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void showTimePickerDialog(View v) {
        DialogFragment newTimeFragment = new TimePickerFragment();
        newTimeFragment.show(getFragmentManager(), "timePicker");
    }

    //~~ method for saving the fitness event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveFitnessEvent(){
        fitnessItem.setExercise(fitnessItem.getExercise());

        boolean saved = fitnessdb.saveEvent(helper.formatDateTime(fitnessItem.getEventDateTime()), 2,0,null,fitnessItem.getExercise(),null);
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(FitnessActivity.this, "Exercise Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(FitnessActivity.this, "Error: Exercise Not Saved", Toast.LENGTH_LONG).show();
    }


}
