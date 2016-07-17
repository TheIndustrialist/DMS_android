package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NutritionActivity extends Activity {

    private NutritionEvent nutritionItem;
    DatabaseHelper nutritiondb;
    private EditText nutritionQty;
    private EditText nutritionFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition);

        nutritionItem = new NutritionEvent();
        nutritiondb = DatabaseHelper.getInstance(this); //return reference to master db


        nutritionQty = (EditText) findViewById(R.id.NutritionQty_txt);
        nutritionQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionQty.setText("");
            }
        });
        nutritionQty.addTextChangedListener(nutritionQtyTextWatcher);


        nutritionFood = (EditText) findViewById(R.id.NutritionFood_txt);
        nutritionFood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nutritionFood.setText("");
            }
        });
        nutritionFood.addTextChangedListener(nutritionFoodTextWatcher);

        Button NutritionSaveBtn = (Button) findViewById(R.id.NutritionSave_btn);
        NutritionSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveNutritionEvent();
            }
        });

        Button NutritionStatsBtn = (Button) findViewById(R.id.NutritionStats_btn);
        NutritionStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fire off the activity to do something instead
                //testing
                String dbList = "\n";
                ArrayList<DiabeticEntry> itemList = nutritiondb.getAllItems();

                for (int i=0; i<nutritiondb.getEntryCount(); i++){
                    DiabeticEntry entry = itemList.get(i);
                    dbList += "\n" + entry.getTime() +
                            "\t" + entry.getCode() +
                            "\t" + entry.getBGL() +
                            "\t" + entry.getDiet() +
                            "\t" + entry.getExercise() +
                            "\t" + entry.getMedication();
                }
                Log.v("DATABASE RECORDS", dbList);
            }
        });

        Button NutritionScriptBtn = (Button) findViewById(R.id.NutritionScript_btn);
        NutritionScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the nutrition prescription db
            }
        });
    }

    private TextWatcher nutritionQtyTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            //CATCH AN EXCEPTION WHEN THE INPUT IS NOT A NUMBER
            try {
                nutritionItem.setQty(Integer.parseInt(s.toString()));
            }catch (NumberFormatException e){
                nutritionItem.setQty(0);
            }
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };

    private TextWatcher nutritionFoodTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s,
                                  int start, int before, int count){
            nutritionItem.setNutrition(s.toString());
        }
        public void afterTextChanged(Editable s) {}
        public void beforeTextChanged(CharSequence s,
                                      int start, int count, int after){}
    };


    private void SaveNutritionEvent(){
        nutritionItem.setNutritionEvent(nutritionItem.getQty(),nutritionItem.getNutrition());

        boolean saved = nutritiondb.saveEvent(1,0,nutritionItem.getNutritionEvent(),null,null);
        //int code, int bgl, String diet, String exercise, String medication
        if( saved == true )
            Toast.makeText(NutritionActivity.this, "Food Saved", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(NutritionActivity.this, "Error: Food Not Saved", Toast.LENGTH_LONG).show();
    }
}
