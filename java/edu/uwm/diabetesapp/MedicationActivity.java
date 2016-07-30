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

public class MedicationActivity extends Activity
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private MedicationEvent medicationItem;
    DatabaseHelper medicationdb;
    private EditText medicationQty;
    private EditText medicationMeds;
    private Button DateBtn;
    private Button TimeBtn;
    private AppHelpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication);

        helper = new AppHelpers();
        medicationItem = new MedicationEvent();
        medicationdb = DatabaseHelper.getInstance(this); //return reference to master db

        //~~set up the medication Qty EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        medicationQty = (EditText) findViewById(R.id.MedicationQty_txt);
        medicationQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                medicationQty.setText("");
            }
        });
        medicationQty.addTextChangedListener(medicationQtyTextWatcher);

        //~~set up the medication meds EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        medicationMeds = (EditText) findViewById(R.id.MedicationMeds_txt);
        medicationMeds.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                medicationMeds.setText("");
            }
        });
        medicationMeds.addTextChangedListener(medicationMedsTextWatcher);

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
        Button MedicationSaveBtn = (Button) findViewById(R.id.MedSave_btn);
        MedicationSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMedicationEvent();
            }
        });

        //~~set up the stats button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button MedicationStatsBtn = (Button) findViewById(R.id.MedStats_btn);
        MedicationStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMedicationStats = new Intent(MedicationActivity.this, MedicationStatsActivity.class);
                startActivity(gotoMedicationStats);
            }
        });


        //~~set up the prescription button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button MedicationScriptBtn = (Button) findViewById(R.id.MedScript_btn);
        MedicationScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMedicationScripts = new Intent(MedicationActivity.this, MedicationScriptActivity.class);
                startActivity(gotoMedicationScripts);
            }
        });


        medicationItem.setEventDateTime(Calendar.getInstance());
    }//end OnCreate

    //~~TextWatcher method for the qty EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher medicationQtyTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                medicationItem.setQty(Integer.parseInt(s.toString()));
            }catch (NumberFormatException e){
                medicationItem.setQty(0);
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~TextWatcher method for the meds EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher medicationMedsTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            medicationItem.setMedication(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        TimeBtn.setText(helper.formatTime(hour,minute));
        medicationItem.setEventDateTime(hour,minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        DateBtn.setText(helper.formatDate(year,month+1,day));
        medicationItem.setEventDateTime(year,month,day);

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

    //~~ method for saving the medication event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveMedicationEvent(){
        medicationItem.setMedicationEvent(medicationItem.getQty(),medicationItem.getMedication());

        boolean saved = medicationdb.saveEvent(helper.formatDateTime(medicationItem.getEventDateTime()), 3,0,null,null,medicationItem.getMedicationEvent());
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(MedicationActivity.this, "Medication Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MedicationActivity.this, "Error: Medication Not Saved", Toast.LENGTH_LONG).show();
    }
}


