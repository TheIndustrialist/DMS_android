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

public class NutritionActivity extends Activity
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private NutritionEvent nutritionItem;
    DatabaseHelper nutritiondb;
    private EditText nutritionQty;
    private EditText nutritionFood;
    private Button DateBtn;
    private Button TimeBtn;
    private AppHelpers helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition);

        helper = new AppHelpers();
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
                Intent gotoNutritionScripts = new Intent(NutritionActivity.this, NutritionScriptActivity.class);
                startActivity(gotoNutritionScripts);
            }
        });

        nutritionItem.setEventDateTime(Calendar.getInstance());
    }//end OnCreate

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

    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        TimeBtn.setText(helper.formatTime(hour,minute));
        nutritionItem.setEventDateTime(hour,minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        DateBtn.setText(helper.formatDate(year,month+1,day));
        nutritionItem.setEventDateTime(year,month,day);

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

    //~~ method for saving the nutrition event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveNutritionEvent(){
        nutritionItem.setNutritionEvent(nutritionItem.getQty(),nutritionItem.getNutrition());
        //nutritionItem.setTime(nutritionTime.getText().toString()); //in case the user didn't change it

        boolean saved = nutritiondb.saveEvent(helper.formatDateTime(nutritionItem.getEventDateTime()),1,0,nutritionItem.getNutritionEvent(),null,null);
        //String dateTime, int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(NutritionActivity.this, "Food Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NutritionActivity.this, "Error: Food Not Saved", Toast.LENGTH_LONG).show();
    }
}
