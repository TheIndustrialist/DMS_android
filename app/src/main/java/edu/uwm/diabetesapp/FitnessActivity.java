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



public class FitnessActivity extends Activity {

    private FitnessEvent fitnessItem;
    DatabaseHelper fitnessdb;
    private EditText fitnessQty;
    private EditText fitnessExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        fitnessItem = new FitnessEvent();
        fitnessdb = DatabaseHelper.getInstance(this); //return reference to master db


        fitnessQty = (EditText) findViewById(R.id.FitnessQty_txt);
        fitnessQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fitnessQty.setText("");
            }
        });
        fitnessQty.addTextChangedListener(fitnessQtyTextWatcher);


        fitnessExercise = (EditText) findViewById(R.id.FitnessExercise_txt);
        fitnessExercise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fitnessExercise.setText("");
            }
        });
        fitnessExercise.addTextChangedListener(fitnessExerciseTextWatcher);


        Button FitnessSaveBtn = (Button) findViewById(R.id.FitnessSave_btn);
        FitnessSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveFitnessEvent();
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
                //testing deleting the db
                fitnessdb.clearDatabase();
            }
        });
}


    private TextWatcher fitnessQtyTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                fitnessItem.setQty(Integer.parseInt(s.toString()));
            }catch (NumberFormatException e){
                fitnessItem.setQty(0);
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    private TextWatcher fitnessExerciseTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
                fitnessItem.setExercise(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };


    private void SaveFitnessEvent(){
        fitnessItem.setFitnessEvent(fitnessItem.getQty(),fitnessItem.getExercise());

        boolean saved = fitnessdb.saveEvent(2,0,null,fitnessItem.getFitnessEvent(),null);
        //int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(FitnessActivity.this, "Exercise Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(FitnessActivity.this, "Error: Exercise Not Saved", Toast.LENGTH_LONG).show();
    }


}
