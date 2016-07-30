package edu.uwm.diabetesapp;

import java.util.Calendar;

public class MedicationEvent {
    private int qty;
    private String medication;
    private String medicationEvent;
    private Calendar eventDateTime;

    public MedicationEvent() {
        medicationEvent = null;
    }

    public void setQty(int amount){
        qty = amount;
    }
    public int getQty(){
        return qty;
    }

    public void setMedication(String meds){
        medication = meds;
    }
    public String getMedication(){return medication;}

    public void setMedicationEvent(int qty, String item){
        medicationEvent = qty + " mg of " + item;
    }
    public String getMedicationEvent() {
        return medicationEvent;
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
