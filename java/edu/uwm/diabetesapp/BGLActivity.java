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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BGLActivity extends Activity
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private SeekBar SeekBGL;
    private TextView selectedBGL;
    private BGLLevel bglLevel;
    private Button DateBtn;
    private Button TimeBtn;
    private AppHelpers helper;

    static final int MINBGL = 40; //minimum BGL
    static final double BGLSCALE = 1.6; //scalar for seekbar
    //40-200

    DatabaseHelper diabeticdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgl);

        helper = new AppHelpers();
        bglLevel = new BGLLevel();
        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db


        //~~set up the BGL entry~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        SeekBGL = (SeekBar) findViewById(R.id.BGLseekBar);
        SeekBGL.setOnSeekBarChangeListener(bglListener);
        selectedBGL = (TextView) findViewById(R.id.textViewBGL);

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
        Button BGLSaveBtn = (Button) findViewById(R.id.BGLSave_btn);
        BGLSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBGL();
            }
        });

        //~~set up the stats button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button BGLStatsBtn = (Button) findViewById(R.id.BGLStats_btn);
        BGLStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayStats();
             }
        });

        //~~set up the prescription button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button BGLScriptBtn = (Button) findViewById(R.id.BGLScript_btn);
        BGLScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoBGLScripts = new Intent(BGLActivity.this, BGLScriptActivity.class);
                startActivity(gotoBGLScripts);
            }
        });

        bglLevel.setEventDateTime(Calendar.getInstance());
    }//end OnCreate


    //~~ChangeListener method for the BGL seekBar~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private SeekBar.OnSeekBarChangeListener bglListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            bglLevel.setBGL(MINBGL + BGLSCALE*(seekBar.getProgress()));
            displayBGL();
        }
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    //~~Helper method to create the textview string to the BGL level~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void displayBGL() {
        String bglText = "BGL: " + String.format("%d",bglLevel.getBGL()) + "mg/dl";
        selectedBGL.setText(bglText);
    }

    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        TimeBtn.setText(helper.formatTime(hour,minute));
        bglLevel.setEventDateTime(hour,minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        DateBtn.setText(helper.formatDate(year,month+1,day));
        bglLevel.setEventDateTime(year,month,day);

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

    //~~ method for saving the BGL event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void saveBGL(){
        //bglLevel.setTime(bglTime.getText().toString()); //in case the user didn't change it
        boolean saved = diabeticdb.saveEvent(helper.formatDateTime(bglLevel.getEventDateTime()), 0,bglLevel.getBGL(),null,null,null);
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(BGLActivity.this, "BGL Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(BGLActivity.this, "Error: BGL Not Saved", Toast.LENGTH_LONG).show();
    }

    //~~ method for displaying the stats~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void displayStats(){
        Intent gotoBGLStats = new Intent(BGLActivity.this, BGLStatsActivity.class);
        startActivity(gotoBGLStats);
    }

    /*
    //~~Helper for formatting the time to 12 hour clock~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private String formatedTime (){
        String dateTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
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
    */
}
