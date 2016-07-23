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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BGLActivity extends Activity {

    private SeekBar SeekBGL;
    private TextView selectedBGL;
    private BGLLevel bglLevel;
    private EditText bglTime;

    static final int MINBGL = 40; //minimum BGL
    static final double BGLSCALE = 1.6; //scalar for seekbar
    //40-200

    DatabaseHelper diabeticdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgl);

        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db
        bglLevel = new BGLLevel();

        //~~set up the BGL entry~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        SeekBGL = (SeekBar) findViewById(R.id.BGLseekBar);
        SeekBGL.setOnSeekBarChangeListener(bglListener);
        selectedBGL = (TextView) findViewById(R.id.textViewBGL);

        //~~set up the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        bglTime = (EditText) findViewById(R.id.BGLTime_txt);
        bglTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
        //TODO set up broadcast listener so this updates??
        bglTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //bglTime.setText("");
            }
        });
        bglTime.addTextChangedListener(bglTimeTextWatcher);

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
                //TODO Build a new activity to edit the BGL prescription db
            }
        });
}


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

    //~~TextWatcher method for the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher bglTimeTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //TODO CATCH AN EXCEPTION WHEN THE INPUT IS NOT LEGIT
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~ method for saving the BGL event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void saveBGL(){
        bglLevel.setTime(bglTime.getText().toString()); //in case the user didn't change it
        boolean saved = diabeticdb.saveEvent(bglLevel.getTime(), 0,bglLevel.getBGL(),null,null,null);
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
}
