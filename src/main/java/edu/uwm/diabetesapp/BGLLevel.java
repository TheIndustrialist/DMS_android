package edu.uwm.diabetesapp;


public class BGLLevel {

    private int bglLevel;
    private String dateTime;

    public BGLLevel() {
        bglLevel = 0;
    }

    public void setBGL(double level){
        bglLevel = (int)level;
    }

    public int getBGL() {
        return bglLevel;
    }

    public void setTime(String datetime){
        dateTime = datetime;
    }

    public String getTime(){return dateTime;}

}
