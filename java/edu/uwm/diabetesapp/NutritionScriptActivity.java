package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Calendar;

public class NutritionScriptActivity extends Activity
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private AppHelpers helper;
    private Button DateFromBtn;
    private Button DateToBtn;
    private Button TimeFromBtn;
    private Button TimeToBtn;
    private Button FreqBtn;
    private Button ActivityBtn;
    private EditText nutritionScript;
    private EditText nutritionScriptQty;
    private NutritionScriptEvent nutritionScriptEvent;
    PrescriptionDatabaseHelper2 prescriptiondb;
    static final int START_DATE = 1;
    static final int END_DATE = 2;
    static final int START_TIME = 1;
    static final int END_TIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_script);

        helper = new AppHelpers();
        nutritionScriptEvent = new NutritionScriptEvent();
        prescriptiondb = PrescriptionDatabaseHelper2.getInstance(this); //return reference to master db

        //~~set up the from Date button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        DateFromBtn = (Button) findViewById(R.id.dateFrom_btn);
        DateFromBtn.setText(helper.getDate()); //init with current date
        DateFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",START_DATE);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //~~set up the to Date button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        DateToBtn = (Button) findViewById(R.id.dateTo_btn);
        DateToBtn.setText(helper.getDate()); //init with current date
        DateToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",END_DATE);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        //~~set up the time from button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        TimeFromBtn = (Button) findViewById(R.id.timeFrom_btn);
        TimeFromBtn.setText(helper.getTime()); //init with current time
        TimeFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",START_TIME);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        //~~set up the time to button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        TimeToBtn = (Button) findViewById(R.id.timeTo_btn);
        TimeToBtn.setText(helper.getTime()); //init with current time
        TimeToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",END_TIME);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        //~~set up the frequency button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        FreqBtn = (Button) findViewById(R.id.freq_btn);
        FreqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(NutritionScriptActivity.this, FreqBtn); //Creating the instance of PopupMenu
                popup.getMenuInflater().inflate(R.menu.script_freq_menu, popup.getMenu()); //Inflating the Popup using xml file
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { //registering popup with OnMenuItemClickListener
                    public boolean onMenuItemClick(MenuItem item) {
                        FreqBtn.setText(item.getTitle());
                        nutritionScriptEvent.setFrequency(helper.convertFreq(item.getTitle().toString()));
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        }); //if no frequency is set, it's a one time event


        //~~set up the nutrition Qty EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        nutritionScriptQty = (EditText) findViewById(R.id.NutritionScriptQty_txt);
        nutritionScriptQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionScriptQty.setText("");
            }
        });
        nutritionScriptQty.addTextChangedListener(nutritionScriptQtyTextWatcher);

        //~~set up the nutrition EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        nutritionScript = (EditText) findViewById(R.id.NutritionScript_txt);
        nutritionScript.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionScript.setText("");
            }
        });
        nutritionScript.addTextChangedListener(nutritionScriptTextWatcher);

        //~~set up the save button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button NutritionSaveBtn = (Button) findViewById(R.id.NutritionScriptSave_btn);
        NutritionSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNutritionEvent();
            }
        });

        //~~set up the edit button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button NutritionEditBtn = (Button) findViewById(R.id.NutritionScriptEdit_btn);
        NutritionEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewNutritionEvents();
            }
        });

        //set up default values, init with OnCreate so they match what's on the view
        nutritionScriptEvent.setEventStart(Calendar.getInstance());
        nutritionScriptEvent.setEventEnd(Calendar.getInstance());



    }//end onCreate



    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        switch (picker) {
            case START_TIME:
                TimeFromBtn.setText(helper.formatTime(hour,minute));
                nutritionScriptEvent.setEventStart(hour,minute);
                break;
            case END_TIME:
                //TODO check to see that end time is later than time date, if it isn't set end time to start time
                TimeToBtn.setText(helper.formatTime(hour,minute));
                nutritionScriptEvent.setEventEnd(hour,minute);
                break;
        }

    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        switch (picker) {
            case START_DATE:
                DateFromBtn.setText(helper.formatDate(year,month+1,day));
                nutritionScriptEvent.setEventStart(year,month,day);
                break;
            case END_DATE:
                //TODO check to see that end date is later than start date, if it isn't set end date to start date
                DateToBtn.setText(helper.formatDate(year,month+1,day));
                nutritionScriptEvent.setEventEnd(year,month,day);
                break;
        }
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


    //~~TextWatcher method for the nutrition Qty EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher nutritionScriptQtyTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                nutritionScriptEvent.setQty(Integer.parseInt(s.toString()));
            }catch (NumberFormatException e){
                nutritionScriptEvent.setQty(0);
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~TextWatcher method for the nutrition EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private TextWatcher nutritionScriptTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            nutritionScriptEvent.setNutrition(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    //~~ method for saving the nutrition prescription event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveNutritionEvent(){
        nutritionScriptEvent.setNutritionEvent(nutritionScriptEvent.getQty(),nutritionScriptEvent.getNutrition());

        boolean saved = prescriptiondb.savePrescription(
                1, //nutrition code
                helper.findNextOccurrence(nutritionScriptEvent.getEventStart(),nutritionScriptEvent.getFrequency()),//next occurrence
                nutritionScriptEvent.getFrequency(),//frequency
                nutritionScriptEvent.getEventEnd(), //endEvent
                null, //bgl
                nutritionScriptEvent.getNutritionEvent(),//nutrition
                null,null); //exercise, medication
        //(int code, long nextOccurrence, long frequency, long endEvent,int bgl, String diet, String exercise, String medication)
        if( saved == true )
            Toast.makeText(NutritionScriptActivity.this, "Nutrition Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NutritionScriptActivity.this, "Error: Nutrition Not Saved", Toast.LENGTH_LONG).show();

    }

    //~~ method for view/edit the nutrition prescription events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void ViewNutritionEvents() {
        Intent gotoNutritionScriptEdit = new Intent(NutritionScriptActivity.this, NutritionScriptViewActivity.class);
        startActivity(gotoNutritionScriptEdit);
    }


}//end activity
