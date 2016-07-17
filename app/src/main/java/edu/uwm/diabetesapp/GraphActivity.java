package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GraphActivity extends Activity {

    DatabaseHelper diabeticdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphstats);

        diabeticdb = DatabaseHelper.getInstance(this); //retrun reference to master db

        //TODO create the main stats and graphs view

    }
}
