package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;

public class StatsActivity extends Activity{

    DatabaseHelper diabeticdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        diabeticdb = DatabaseHelper.getInstance(this); //return reference to master db

        //TODO run all the listeners for the editTexts
        //TODO create the main stats and graphs view
    }
}
