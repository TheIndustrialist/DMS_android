package edu.uwm.diabetesapp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppHelpers {

    public String getDate(){
        String Date =  new SimpleDateFormat("EEE, MMM dd, yyyy").format(Calendar.getInstance().getTime());
        return Date;
    }

    public String getTime(){
        String Time =  new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());
        return Time;
    }

    public String formatTime (int hourOfDay, int minute){ //returns a pretty string
        int hour = hourOfDay;
        String min;

        if (minute<10){
            min = "0" + Integer.toString(minute);
        } else {
            min = Integer.toString(minute);
        }

        if (hour<12) {
            if (hour==0) {
                hour+=12;//midnight to 12:59AM
            }
            return Integer.toString(hour) + ":" + min + " AM";
        }  else {
            if (hour>12) {
                hour-=12;//1:00PM to 11:59PM
            } //else noon to 12:59PM
            return Integer.toString(hour) + ":" + min + " PM";
        }
    }

    public String formatDate (int year, int month, int day){ //makes a pretty date
        String dateString = Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
        SimpleDateFormat dateIn = new SimpleDateFormat("MM/dd/yyyy");

        Date convertedDate = new Date();
        try {
            convertedDate = dateIn.parse(dateString);
        } catch (ParseException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String dateOut = new SimpleDateFormat("EEE, MMM dd, yyyy").format(convertedDate);
        return dateOut;
    }

    public long findNextOccurrence(long curOccurrence, long frequency){
        /*curOccurrence is in epoch time, frequency is in minutes.
        * Add them together to find the next occurrence after the current time*/
        long next_occurrence = curOccurrence;
        if (frequency==0)frequency=1;//add one minute to keep from getting stuck in the while loop
        //long freq_in_mils = frequency*60*1000; //frequency is in minutes, convert to milliseconds
        long freq_in_secs = frequency*60; //frequency is in minutes, convert to seconds
        long currentTime = System.currentTimeMillis()/1000;

        //if this occurrence happened in the past, add the frequency to find the next occurrence
        //if it happens in the future, it will be the next occurrence so just return it
        while (next_occurrence <= currentTime){
            next_occurrence+=freq_in_secs;
        }
        return next_occurrence;
    }

    public String formatDateTime(Calendar calendar){
        //Date date = new Date(epoch*1000);  //set a date to epoch in milliseconds
        //String Time =  new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());


        String dateTime =  new SimpleDateFormat("hh:mm aa EEE, MMM dd, yyyy").format(calendar.getTime());
        return dateTime;
    }


    //~~ method for converting the entered string frequency into hours~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public long convertFreq(String string){
        switch(string){ //ref menu/script_freq_menu.xml
            case "Frequency":
                return 0; //one time event at the scheduled time
            case "Hourly":
                return 1*60;
            case "Daily":
                return 24*60;
            case "Weekly":
                return 168*60;
            case "Monthly":
                return 168*60*30;//TODO figure out the month and year
            case "Yearly":
                return 8736*60;//based on 365days
            default:
                return 0;
        } //end switch
        //TODO add more entries like 15 minutes, 2 hours, etc to make more granular
    }

    public String formatDatTime(long epoch){
        Date date = new Date(epoch*1000);  //set a date to epoch in milliseconds
        String dateTime =  new SimpleDateFormat("hh:mm aa EEE, MMM dd, yyyy").format(date);
        return dateTime;
    }

    public String formatFrequency(long freqMin){ //takes in frequency in minutes and returns a pretty string
        if (freqMin==0) return ("once");
        else if (freqMin<60) return ("every " + freqMin + " minutes");
        else if (freqMin==60) return("every hour");
        else if (freqMin<1440) return("every " + (freqMin/60) + " hours");
        else if (freqMin==1440) return("every day");
        else if (freqMin<10080) return("every " + (freqMin/1440) + " days");
        else if (freqMin==10080) return("every week");
        else return ("every " + freqMin + " minutes"); //TODO figure out month and year
    }


}
