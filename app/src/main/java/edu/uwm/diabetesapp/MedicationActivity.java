package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MedicationActivity extends Activity {

    private MedicationEvent medicationItem;
    DatabaseHelper medicationdb;
    private EditText medicationQty;
    private EditText medicationMeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication);

        medicationItem = new MedicationEvent();
        medicationdb = DatabaseHelper.getInstance(this); //return reference to master db


        medicationQty = (EditText) findViewById(R.id.MedicationQty_txt);
        medicationQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                medicationQty.setText("");
            }
        });
        medicationQty.addTextChangedListener(medicationQtyTextWatcher);


        medicationMeds = (EditText) findViewById(R.id.MedicationMeds_txt);
        medicationMeds.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                medicationMeds.setText("");
            }
        });
        medicationMeds.addTextChangedListener(medicationMedsTextWatcher);

        Button MedicationSaveBtn = (Button) findViewById(R.id.MedSave_btn);
        MedicationSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMedicationEvent();


        Button MedicationStatsBtn = (Button) findViewById(R.id.MedStats_btn);
        MedicationStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fire off the activity to do something instead
                //testing
                String dbList = "\n";
                ArrayList<DiabeticEntry> itemList = medicationdb.getAllItems();

                for (int i=0; i<medicationdb.getEntryCount(); i++){
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
            }
        });

        Button MedicationScriptBtn = (Button) findViewById(R.id.MedScript_btn);
        MedicationScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the medication prescription db
            }
        });
    }

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

    private TextWatcher medicationMedsTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            medicationItem.setMedication(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };


    private void SaveMedicationEvent(){
        medicationItem.setMedicationEvent(medicationItem.getQty(),medicationItem.getMedication());

        boolean saved = medicationdb.saveEvent(3,0,null,null,medicationItem.getMedicationEvent());
        //int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(MedicationActivity.this, "Medication Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MedicationActivity.this, "Error: Medication Not Saved", Toast.LENGTH_LONG).show();
    }
}


