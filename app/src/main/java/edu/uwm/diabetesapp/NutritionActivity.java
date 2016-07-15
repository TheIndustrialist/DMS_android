package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NutritionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition);

        //TODO change the top button into a textviews for entering food items

        Button NutritionSaveBtn = (Button) findViewById(R.id.NutritionSave_btn);
        NutritionSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Save the nutrition to the nutrition db
            }
        });

        Button NutritionStatsBtn = (Button) findViewById(R.id.NutritionStats_btn);
        NutritionStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to show the nutrition db stats
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
}
