package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/16/2016.
 */
public class NutritionEvent {
    private int qty;
    private String nutrition;
    private String nutritionEvent;

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

}
