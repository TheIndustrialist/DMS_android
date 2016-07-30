package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/14/2016.
 */
public class DiabeticEntry {

    //MEMBER ATTRIBUTES
    private long index;
    private String timestamp;
    private int code;
    private int BGL;
    private String diet;
    private String exercise;
    private String medication;

    public DiabeticEntry(){}

    public DiabeticEntry(long idx, String time, int id, int bgl, String nutrition, String fitness, String meds){
        index = idx;
        timestamp = time;
        code = id;
        BGL = bgl;
        diet = nutrition;
        exercise = fitness;
        medication = meds;
    }

    public void setIndex(long id){
        index = id;
    }
    public long getIndex(){
        return index;
    }

    public void setTime(String time){
        timestamp = time;
    }
    public String getTime(){
        return timestamp;
    }

    public void setCode(int id){
        code = id;
    }
    public int getCode(){
        return code;
    }

    public void setBGL(int bgl){
        BGL = bgl;
    }
    public int getBGL(){
        return BGL;
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

    public void setMedication(String meds){
        medication = meds;
    }
    public String getMedication(){
        return medication;
    }
}
