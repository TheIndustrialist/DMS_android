package edu.uwm.diabetesapp;

import java.util.Calendar;

public class NutritionEvent {
    private int qty;
    private String nutrition;
    private String nutritionEvent;
    //private String dateTime;
    private Calendar eventDateTime;

    public NutritionEvent() {
        nutritionEvent = null;
    }


    public void setQty(int amount){
        qty = amount;
    }

    public int getQty(){
        return qty;
    }

    public void setNutrition(String food){
        nutrition = food;
    }

    public String getNutrition(){return nutrition;}

    public void setNutritionEvent(int qty, String item){
        nutritionEvent = qty + " servings " + item;
    }

    public String getNutritionEvent() {
        return nutritionEvent;
    }

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
