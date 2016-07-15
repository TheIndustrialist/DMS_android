package edu.uwm.diabetesapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MedicationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication);

        //TODO change the top button into a textviews for entering meds taken

        Button MedicationSaveBtn = (Button) findViewById(R.id.MedSave_btn);
        MedicationSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Save the medication to the medication db
            }
        });

        Button MedicationStatsBtn = (Button) findViewById(R.id.MedStats_btn);
        MedicationStatsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to show the medication db stats
            }
        });

        Button MedicationScriptBtn = (Button) findViewById(R.id.MedScript_btn);
        MedicationScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Build a new activity to edit the medication prescription db
            }
        });
    }
}
