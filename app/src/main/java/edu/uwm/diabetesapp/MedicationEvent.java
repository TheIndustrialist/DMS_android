package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/16/2016.
 */
public class MedicationEvent {
    private int qty;
    private String medication;
    private String medicationEvent;

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
        medicationEvent = qty + " mg " + item;
    }

    public String getMedicationEvent() {
        return medicationEvent;
    }

}
