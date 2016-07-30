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

public class BGLScriptActivity extends  Activity
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private AppHelpers helper;
    private Button DateFromBtn;
    private Button DateToBtn;
    private Button TimeFromBtn;
    private Button TimeToBtn;
    private Button FreqBtn;
    private Button ActivityBtn;
    private EditText fitnessScript;
    private BGLScriptEvent bglScriptEvent;
    PrescriptionDatabaseHelper2 prescriptiondb;
    static final int START_DATE = 1;
    static final int END_DATE = 2;
    static final int START_TIME = 1;
    static final int END_TIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bgl_script);

        helper = new AppHelpers();
        bglScriptEvent = new BGLScriptEvent();
        prescriptiondb = PrescriptionDatabaseHelper2.getInstance(this); //return reference to master db

        //~~set up the from Date button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        DateFromBtn = (Button) findViewById(R.id.dateFrom_btn);
        DateFromBtn.setText(helper.getDate()); //init with current date
        DateFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE", START_DATE);
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
                bundle.putInt("DATE", END_DATE);
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
                bundle.putInt("TIME", START_TIME);
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
                bundle.putInt("TIME", END_TIME);
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
                PopupMenu popup = new PopupMenu(BGLScriptActivity.this, FreqBtn); //Creating the instance of PopupMenu
                popup.getMenuInflater().inflate(R.menu.script_freq_menu, popup.getMenu()); //Inflating the Popup using xml file
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { //registering popup with OnMenuItemClickListener
                    public boolean onMenuItemClick(MenuItem item) {
                        FreqBtn.setText(item.getTitle());
                        bglScriptEvent.setFrequency(helper.convertFreq(item.getTitle().toString()));
                        return true;
                    }
                });
                popup.show(); //showing popup menu
            }
        }); //if no frequency is set, it's a one time event

/*
        //~~set up the fitness EditText~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        fitnessScript = (EditText) findViewById(R.id.fitnessScript_txt);
        fitnessScript.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                fitnessScript.setText("");
            }
        });
        fitnessScript.addTextChangedListener(fitnessScriptTextWatcher);
*/
        //~~set up the save button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button BGLSaveBtn = (Button) findViewById(R.id.BGLScriptSave_btn);
        BGLSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveBGLEvent();
            }
        });

        //~~set up the edit button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button BGLEditBtn = (Button) findViewById(R.id.BGLScriptEdit_btn);
        BGLEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewBGLEvents();
            }
        });

        //set up default values, init with OnCreate so they match what's on the view
        bglScriptEvent.setEventStart(Calendar.getInstance());
        bglScriptEvent.setEventEnd(Calendar.getInstance());


    }//end onCreate


    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        switch (picker) {
            case START_TIME:
                TimeFromBtn.setText(helper.formatTime(hour, minute));
                bglScriptEvent.setEventStart(hour, minute);
                break;
            case END_TIME:
                //TODO check to see that end time is later than time date, if it isn't set end time to start time
                TimeToBtn.setText(helper.formatTime(hour, minute));
                bglScriptEvent.setEventEnd(hour, minute);
                break;
        }

    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        switch (picker) {
            case START_DATE:
                DateFromBtn.setText(helper.formatDate(year, month + 1, day));
                bglScriptEvent.setEventStart(year, month, day);
                break;
            case END_DATE:
                //TODO check to see that end date is later than start date, if it isn't set end date to start date
                //TODO extend end date if frequency is larger or just get rid of the end date all together since the entries would be editable?
                DateToBtn.setText(helper.formatDate(year, month + 1, day));
                bglScriptEvent.setEventEnd(year, month, day);
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


    //~~ method for saving the fitness prescription event~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void SaveBGLEvent() {
        bglScriptEvent.setBGLEvent("Check BGL");

        boolean saved = prescriptiondb.savePrescription(
                0, //BGL code
                helper.findNextOccurrence(bglScriptEvent.getEventStart(), bglScriptEvent.getFrequency()),//next occurrence
                bglScriptEvent.getFrequency(),//frequency
                bglScriptEvent.getEventEnd(), //endEvent
                bglScriptEvent.getBGLEvent(),
                null, //nutrition
                null,//exercise
                null); //medication
        //(int code, long nextOccurrence, long frequency, long endEvent,int bgl, String diet, String exercise, String medication)
        if (saved == true)
            Toast.makeText(BGLScriptActivity.this, "BGL Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(BGLScriptActivity.this, "Error: BGL Not Saved", Toast.LENGTH_LONG).show();

    }

    //~~ method for view/edit the fitness prescription events~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void ViewBGLEvents() {
        Intent gotoBGLScriptEdit = new Intent(BGLScriptActivity.this, BGLScriptViewActivity.class);
        startActivity(gotoBGLScriptEdit);
    }

}
