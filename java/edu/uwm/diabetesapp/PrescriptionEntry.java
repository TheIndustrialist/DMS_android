package edu.uwm.diabetesapp;

public class PrescriptionEntry {

    //MEMBER ATTRIBUTES
    private int id;
    private int code;
    private long frequency;
    private long eventEnd;
    private long nextOccurrence;
    private String bgl;
    private String diet;
    private String exercise;
    private String medication;

    public PrescriptionEntry(){}

    public void setID(int ID) {id = ID;}
    public int getID() {return id;}

    public void setCode(int CODE){
        code = CODE;
    }
    public int getCode(){
        return code;
    }

    public void setFrequency(long FREQUENCY){frequency = FREQUENCY;}
    public long getFrequency(){
        return frequency;
    }

    public void setNextOccurrence(long nextoccurrence){nextOccurrence = nextoccurrence;}
    public long getNextOccurrence(){
        return nextOccurrence;
    }

    public void setEventEnd(long eventend){eventEnd = eventend;}
    public long getEventEnd(){
        return eventEnd;
    }

    public void setBGL(String checkBGL){
        bgl = checkBGL;
    }
    public String getBGL(){
        return bgl;
    }

    public void setDiet(String nutrition){
        diet = nutrition;
    }
    public String getDiet(){
        return diet;
    }

    public void setExercise(String fitness){
        exercise = fitness;
    }
    public String getExercise(){
        return exercise;
    }

    public void setMedication(String meds) {medication = meds;}
    public String getMedication(){return medication; }



}
