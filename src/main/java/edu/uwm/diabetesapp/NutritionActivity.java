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

public class NutritionActivity extends Activity {

    private NutritionEvent nutritionItem;
    DatabaseHelper nutritiondb;
    private EditText nutritionQty;
    private EditText nutritionFood;
    private EditText nutritionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition);

        nutritionItem = new NutritionEvent();
        nutritiondb = DatabaseHelper.getInstance(this); //return reference to master db

        //~~set up the diet Qty EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        nutritionQty = (EditText) findViewById(R.id.NutritionQty_txt);
        nutritionQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionQty.setText(""); //re-clear this out for multiple entries
            }
        });
        nutritionQty.addTextChangedListener(nutritionQtyTextWatcher);

        //~~set up the diet food EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        nutritionFood = (EditText) findViewById(R.id.NutritionFood_txt);
        nutritionFood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionFood.setText(""); //re-clear this out for multiple entries
            }
        });
        nutritionFood.addTextChangedListener(nutritionFoodTextWatcher);

        //~~set up the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        nutritionTime = (EditText) findViewById(R.id.NutritionTime_txt);
        nutritionTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime()));
        //TODO set up broadcast listener so this updates??
        nutritionTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionTime.setText("");
            }
        });
        nutritionTime.addTextChangedListener(nutritionTimeTextWatcher);

        //~~set up the save button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button NutritionSaveBtn = (Button) findViewById(R.id.NutritionSave_btn);
        NutritionSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNutritionEvent();
            }
        });

        //~~set up the stats button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button NutritionStatsBtn = (Button) findViewById(R.id.NutritionStats_btn);
        NutritionStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNutritionStats = new Intent(NutritionActivity.this, NutritionStatsActivity.class);
                startActivity(gotoNutritionStats);
            }
        });

        //~~set up the prescription button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button NutritionScriptBtn = (Button) findViewById(R.id.NutritionScript_btn);
        NutritionScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the nutrition prescription db
            }
        });
    }

    //~~TextWatcher method for the qty EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher nutritionQtyTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                nutritionItem.setQty(Integer.parseInt(s.toString()));
            }catch (NumberFormatException e){
                nutritionItem.setQty(0);
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~TextWatcher method for the diet item EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher nutritionFoodTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            nutritionItem.setNutrition(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~TextWatcher method for the dateTime EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher nutritionTimeTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //TODO CATCH AN EXCEPTION WHEN THE INPUT IS NOT LEGIT
            //fitnessItem.setTime(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~ method for saving the nutrition event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveNutritionEvent(){
        nutritionItem.setNutritionEvent(nutritionItem.getQty(),nutritionItem.getNutrition());
        nutritionItem.setTime(nutritionTime.getText().toString()); //in case the user didn't change it

        boolean saved = nutritiondb.saveEvent(nutritionItem.getTime(),1,0,nutritionItem.getNutritionEvent(),null,null);
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(NutritionActivity.this, "Food Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NutritionActivity.this, "Error: Food Not Saved", Toast.LENGTH_LONG).show();
    }
}
