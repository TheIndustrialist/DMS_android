package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/14/2016.
 */
public class DiabeticEntry {

    //MEMBER ATTRIBUTES
    private String timestamp;
    private int code;
    private int BGL;
    private String diet;
    private String exercise;
    private String medication;

    public DiabeticEntry(){}

    public DiabeticEntry(String time, int id, int bgl, String nutrition, String fitness, String meds){
        timestamp = time;
        code = id;
        BGL = bgl;
        diet = nutrition;
        exercise = fitness;
        medication = meds;
    }

    public String getTime(){
        return timestamp;
    }

    public void setTime(String time){
        timestamp = time;
    }

    public int getCode(){
        return code;
    }

    public void setCode(int id){
        code = id;
    }

    public int getBGL(){
        return BGL;
    }

    public void setBGL(int bgl){
        BGL = bgl;
    }

    public String getDiet(){
        return diet;
    }

    public void setDiet(String nutrition){
        diet = nutrition;
    }

    public String getExercise(){
        return exercise;
    }

    public void setExercise(String fitness){
        exercise = fitness;
    }

    public String getMedication(){
        return medication;
    }

    public void setMedication(String meds){
        medication = meds;
    }

}
