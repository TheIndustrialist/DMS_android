package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/15/2016.
 */
public class FitnessEvent {

    private int qty;
    private String exercise;
    private String fitnessEvent;

    public FitnessEvent() {
        fitnessEvent = null;
    }



    public void setQty(int amount){
        qty = amount;
    }

    public int getQty(){
        return qty;
    }

    public void setExercise(String exercises){
        exercise = exercises;
    }

    public String getExercise(){return exercise;}


    public void setFitnessEvent(int qty, String item){
        fitnessEvent = qty + " " + item;
    }

    public String getFitnessEvent() {
        return fitnessEvent;
    }
}
