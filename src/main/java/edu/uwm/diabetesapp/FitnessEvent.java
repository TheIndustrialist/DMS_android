package edu.uwm.diabetesapp;

public class FitnessEvent {

    private int qty;
    private String exercise;
    private String fitnessEvent;
    private String dateTime;

    public FitnessEvent() {
        fitnessEvent = null;
    }

    public void setExercise(String exercises){
        exercise = exercises;
    }

    public String getExercise(){return exercise;}

    public void setTime(String datetime){
        dateTime = datetime;
    }

    public String getTime(){return dateTime;}

}
