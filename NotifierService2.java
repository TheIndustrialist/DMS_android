package edu.uwm.diabetesapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;




public class NotifierService2 extends Service {

    public static final long NOTIFY_INTERVAL = 60 * 1000; // 60 seconds

    private Handler mHandler = new Handler(); // run on another Thread to avoid crash
    private Timer mTimer = null; // timer handling
    PrescriptionDatabaseHelper2 prescriptiondb;
    NotificationCompat.Builder mBuilder;
    int mNotificationId;
    ArrayList<PrescriptionEntry> prescriptionsDue = new ArrayList<>();
    PrescriptionEntry prescription;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        prescriptiondb = PrescriptionDatabaseHelper2.getInstance(this); //return reference to master db

        if (mTimer != null) { // cancel if already existed
            mTimer.cancel();
        } else { // recreate new
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);// schedule task
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() { // run on another thread
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "Timer",Toast.LENGTH_SHORT).show();
                    prescriptionsDue = PrescriptionItemDue(); //localize the prescriptionEntries which are due
                    if (prescriptionsDue.size()>0) {
                        //Toast.makeText(getApplicationContext(), "Item Due",Toast.LENGTH_SHORT).show();
                        //TODO scan through the prescription items that are due and populate the notification
                        NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);// Gets an instance of the NotificationManager service
                        mNotificationId = 000;
                        String title;
                        String text;

                        for (int i=0; i<prescriptionsDue.size(); i++){
                            text = "";
                            title = "Diabetic Reminder";
                            prescription = prescriptionsDue.get(i);

                            switch(prescription.getCode()) {
                                case 0:
                                    text = "Check BGL";
                                    break;
                                case 1: //diet
                                    text = "Eat " + prescription.getDiet();
                                    break;
                                case 2: //exercise
                                    text = prescription.getExercise();
                                    break;
                                case 3: //medication
                                    text = "Take " + prescription.getMedication();
                                    break;
                                default:
                                    text = "Reminder due";
                                    break;
                            }

                            /*
                            * Check BGL
                            * Eat 12 servings of apple
                            * run 2 miles
                            * take 15mg drugs
                            * */


                            mBuilder = new NotificationCompat.Builder(NotifierService2.this)
                                    .setSmallIcon(R.drawable.appicon)
                                    //.setContentTitle("My notification")
                                    //.setContentText("Hello World!");
                                    .setContentTitle(title)
                                    .setContentText(text);

                            mNotificationId++;
                            mNotifyMgr.notify(mNotificationId, mBuilder.build()); // Builds the notification and issues it.

                        }
                    }
                    //else Toast.makeText(getApplicationContext(), "No item Due",Toast.LENGTH_SHORT).show();
                    //TODO toast for testing, notifier for reals
                }
            });
        } //end run


        //~~Method to return an array list of off due prescription events
        private ArrayList PrescriptionItemDue() {
            ArrayList<PrescriptionEntry> allPrescriptionsArray = prescriptiondb.getAllItems();
            ArrayList<PrescriptionEntry> prescriptionsDue = new ArrayList<>();
            String dbList = "\n"; //for the log window
            int prescriptionIndex = 0;
            long currentTime = System.currentTimeMillis()/1000;

            for (int i=0; i<prescriptiondb.getEntryCount(); i++){
                PrescriptionEntry entry = allPrescriptionsArray.get(i);
                if ((entry.getNextOccurrence()<=currentTime)&& (entry.getEventEnd()>currentTime)){
                    //We're at or past an occurrence and the end of the event is in the future
                    //put it in the prescriptionsDue arraylist and increment the prescriptionindex
                    prescriptionsDue.add(prescriptionIndex, entry);
                    prescriptionIndex++;
                    //dbList += "\n" + entry.getNextOccurrence() + "\t" + entry.getFrequency() + "\t" +

                    //call a routine to increment the next occurrence by the frequency
                    if (prescriptiondb.forwardPrescription(i)==false)
                       return null;
                }
            }
            Log.v("NOTIFIER", dbList);
            return prescriptionsDue;
        }

    }//end time display timer task class






}//end class
