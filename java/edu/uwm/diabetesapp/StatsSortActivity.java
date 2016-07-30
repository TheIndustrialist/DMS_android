package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class StatsSortActivity extends Activity
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
        setContentView(R.layout.stats);

        helper = new AppHelpers();
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
        ActivityBtn = (Button) findViewById(R.id.activity_btn);
        ActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(StatsSortActivity.this, ActivityBtn); //Creating the instance of PopupMenu
                popup.getMenuInflater().inflate(R.menu.activity_select_menu, popup.getMenu()); //Inflating the Popup using xml file
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { //registering popup with OnMenuItemClickListener
                    public boolean onMenuItemClick(MenuItem item) {
                        ActivityBtn.setText(item.getTitle());
                        //TODO change the fragment to allow min/max bgl or keyword search depending on selection
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        });


        //~~set up the edit button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button ActivityViewBtn = (Button) findViewById(R.id.ActivityView_btn);
        ActivityViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO launch the graphs or table views
            }
        });
    }//end onCreate



    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        switch (picker) {
            case START_TIME:
                TimeFromBtn.setText(helper.formatTime(hour,minute));
                //TODO
                break;
            case END_TIME:
                //TODO check to see that end time is later than time date, if it isn't set end time to start time
                TimeToBtn.setText(helper.formatTime(hour,minute));
                //TODO
                break;
        }

    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        switch (picker) {
            case START_DATE:
                DateFromBtn.setText(helper.formatDate(year,month+1,day));
                //TODO
                break;
            case END_DATE:
                //TODO check to see that end date is later than start date, if it isn't set end date to start date
                DateToBtn.setText(helper.formatDate(year,month+1,day));
                //TODO
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



}//end activity