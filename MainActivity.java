package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;

public class MainActivity extends Activity {

    DatabaseHelper diabeticdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //diabeticdb = new DatabaseHelper(this);
        diabeticdb = DatabaseHelper.getInstance(this);

        //~~set up the BGL button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button BGLBtn = (Button) findViewById(R.id.BGLbutton);
        BGLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoBGL = new Intent(MainActivity.this, BGLActivity.class);
                startActivity(gotoBGL);
            }
        });

        //~~set up the nutrition button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button NutritionBtn = (Button) findViewById(R.id.Nutritionbutton);
        NutritionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoNutrition = new Intent(MainActivity.this, NutritionActivity.class);
                startActivity(gotoNutrition);
            }
        });

        //~~set up the fitness button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button FitnessBtn = (Button) findViewById(R.id.Fitnessbutton);
        FitnessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFitness = new Intent(MainActivity.this, FitnessActivity.class);
                startActivity(gotoFitness);
            }
        });

        //~~set up the medication button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button MedicationBtn = (Button) findViewById(R.id.Medicationbutton);
        MedicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoMedication = new Intent(MainActivity.this, MedicationActivity.class);
                startActivity(gotoMedication);
            }
        });

        //~~set up the stats button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button StatsBtn = (Button) findViewById(R.id.Statsbutton);
        StatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoStats = new Intent(MainActivity.this, StatsSortActivity.class);
                startActivity(gotoStats);
            }
        });

        //~~ set up the notifier service~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Intent intent = new Intent(this, NotifierService2.class);
        startService(intent);



    }//end OnCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void Notify(String notificationTitle, String notificationMessage){
        /*
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.appicon,"New Message", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,NotificationView.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        notification.setLatestEventInfo(MainActivity.this, notificationTitle,notificationMessage, pendingIntent);
        notificationManager.notify(9999, notification);
        */
    }

}//end class
