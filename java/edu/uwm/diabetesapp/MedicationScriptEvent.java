package edu.uwm.diabetesapp;


import java.util.Calendar;

public class MedicationScriptEvent {
    private int qty;
    private String medication;
    private String medicationEvent;
    private long frequency;
    private Calendar eventEnd;
    private Calendar eventStart;

    public MedicationScriptEvent() {
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

    public void setFrequency(long FREQUENCY){frequency = FREQUENCY;}
    public long getFrequency(){
        return frequency;
    }

    public void setEventEnd(Calendar eventend){eventEnd = eventend;} //initing
    public void setEventEnd(int hour,int minute){
        eventEnd.set(Calendar.HOUR_OF_DAY,hour);
        eventEnd.set(Calendar.MINUTE,minute);
    } //when time changed
    public void setEventEnd(int year,int month, int day){
        eventEnd.set(Calendar.YEAR,year);
        eventEnd.set(Calendar.MONTH,month);
        eventEnd.set(Calendar.DAY_OF_MONTH,day);
    } //when date changed
    public long getEventEnd(){return eventEnd.getTimeInMillis()/1000;}

    public void setEventStart(Calendar eventstart){eventStart = eventstart;}
    public void setEventStart(int hour,int minute){
        eventStart.set(Calendar.HOUR_OF_DAY,hour);
        eventStart.set(Calendar.MINUTE,minute);
    }
    public void setEventStart(int year,int month, int day){
        eventStart.set(Calendar.YEAR,year);
        eventStart.set(Calendar.MONTH,month);
        eventStart.set(Calendar.DAY_OF_MONTH,day);
    }
    public long getEventStart(){return eventStart.getTimeInMillis()/1000;}
}
