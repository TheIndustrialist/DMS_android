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

public class MedicationActivity extends Activity {

    private MedicationEvent medicationItem;
    DatabaseHelper medicationdb;
    private EditText medicationQty;
    private EditText medicationMeds;
    private EditText medicationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication);

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

        //~~set up the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        medicationTime = (EditText) findViewById(R.id.MedicationTime_txt);
        medicationTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
        //TODO set up broadcast listener so this updates??
        medicationTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                medicationTime.setText("");
            }
        });
        medicationTime.addTextChangedListener(medicationTimeTextWatcher);

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
                //TODO Build a new activity to edit the medication prescription db
            }
        });
    }

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

    //~~TextWatcher method for the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher medicationTimeTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //TODO CATCH AN EXCEPTION WHEN THE INPUT IS NOT LEGIT
            //fitnessItem.setTime(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~ method for saving the medication event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveMedicationEvent(){
        medicationItem.setMedicationEvent(medicationItem.getQty(),medicationItem.getMedication());
        medicationItem.setTime(medicationTime.getText().toString()); //in case the user didn't change it

        boolean saved = medicationdb.saveEvent(medicationItem.getTime(), 3,0,null,null,medicationItem.getMedicationEvent());
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(MedicationActivity.this, "Medication Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MedicationActivity.this, "Error: Medication Not Saved", Toast.LENGTH_LONG).show();
    }
}


