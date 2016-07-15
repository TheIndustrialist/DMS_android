package edu.uwm.diabetesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class BGLActivity extends Activity {

    private SeekBar SeekBGL;
    private TextView selectedBGL;
    private BGLLevel bglLevel;

    static final int MINBGL = 40; //minimum BGL
    static final double BGLSCALE = 1.6; //scalar for seekbar
    //40-200

    DatabaseHelper diabeticdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgl);

        diabeticdb = DatabaseHelper.getInstance(this); //retrun reference to master db

        bglLevel = new BGLLevel();

        SeekBGL = (SeekBar) findViewById(R.id.seekBar1);
        selectedBGL = (TextView) findViewById(R.id.textViewBGL);

        registerChangeListener();

        Button BGLSaveBtn = (Button) findViewById(R.id.BGLSave_btn);
        BGLSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBGL();
            }
        });

        Button BGLStatsBtn = (Button) findViewById(R.id.BGLStats_btn);
        BGLStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayStats();
             }
        });

        Button BGLScriptBtn = (Button) findViewById(R.id.BGLScript_btn);
        BGLScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the BGL prescription db
            }
        });
}


    private void registerChangeListener() {
        SeekBGL.setOnSeekBarChangeListener(bglListener);
    }

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


    private void displayBGL() {
        String bglText = "BGL: " + String.format("%d",bglLevel.getBGL()) + "mg/dl";
        selectedBGL.setText(bglText);
    }

    private void saveBGL(){
        boolean saved = diabeticdb.saveEvent(0,bglLevel.getBGL(),"","","");
          //int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(BGLActivity.this, "BGL Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(BGLActivity.this, "Error: BGL Not Saved", Toast.LENGTH_LONG).show();
    }

    private void displayStats(){
        //TODO fire off the activity to do something instead
        //testing
        String dbList = "\n";
        ArrayList<DiabeticEntry> itemList = diabeticdb.getAllItems();

        for (int i=0; i<diabeticdb.getEntryCount(); i++){
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

}
