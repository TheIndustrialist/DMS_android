package edu.uwm.diabetesapp;

import java.util.Calendar;

public class FitnessEvent {

    private int qty;
    private String exercise;
    private String fitnessEvent;
    //private String dateTime;
    private Calendar eventDateTime;

    public FitnessEvent() {
        fitnessEvent = null;
    }

    public void setExercise(String exercises){
        exercise = exercises;
    }

    public String getExercise(){return exercise;}

    public void setEventDateTime(Calendar eventdatetime){eventDateTime = eventdatetime;}
    public void setEventDateTime(int hour,int minute){
        eventDateTime.set(Calendar.HOUR_OF_DAY,hour);
        eventDateTime.set(Calendar.MINUTE,minute);
    }
    public void setEventDateTime(int year,int month, int day){
        eventDateTime.set(Calendar.YEAR,year);
        eventDateTime.set(Calendar.MONTH,month);
        eventDateTime.set(Calendar.DAY_OF_MONTH,day);
    }
    public Calendar getEventDateTime(){
        return eventDateTime;
    }

}
