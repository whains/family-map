package ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import model.DataCache;
import will.familymap.R;

public class SettingsActivity extends AppCompatActivity {

    DataCache data = DataCache.initialize();

    private ToggleButton lifeStoryButton;
    private ToggleButton familyTreeButton;
    private ToggleButton spouseButton;

    private ToggleButton fathersButton;
    private ToggleButton mothersButton;
    private ToggleButton maleButton;
    private ToggleButton femaleButton;

    private TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        lifeStoryButton = (ToggleButton) findViewById(R.id.lifeStoryToggle);
        familyTreeButton = (ToggleButton) findViewById(R.id.familyTreeToggle);
        spouseButton = (ToggleButton) findViewById(R.id.spouseToggle);

        lifeStoryButton.setChecked(data.isLifeStoryLinesOn());
        familyTreeButton.setChecked(data.isFamilyTreeLinesOn());
        spouseButton.setChecked(data.isSpouseLinesOn());

        fathersButton = (ToggleButton) findViewById(R.id.fatherFilterToggle);
        mothersButton = (ToggleButton) findViewById(R.id.motherFilterToggle);
        maleButton = (ToggleButton) findViewById(R.id.maleFilterToggle);
        femaleButton = (ToggleButton) findViewById(R.id.femaleFilterToggle);

        fathersButton.setChecked(data.isFatherFilterOn());
        mothersButton.setChecked(data.isMotherFilterOn());
        maleButton.setChecked(data.isMaleFilterOn());
        femaleButton.setChecked(data.isFemaleFilterOn());

        logout = (TextView) findViewById(R.id.logout);

        lifeStoryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setLifeStoryLinesOn(true); }
                else { data.setLifeStoryLinesOn(false); }
            }
        });

        familyTreeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setFamilyTreeLinesOn(true); }
                else { data.setFamilyTreeLinesOn(false); }
            }
        });

        spouseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setSpouseLinesOn(true); }
                else { data.setSpouseLinesOn(false); }
            }
        });

        fathersButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setFatherFilterOn(true); }
                else { data.setFatherFilterOn(false); }
            }
        });

        mothersButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setMotherFilterOn(true); }
                else { data.setMotherFilterOn(false); }
            }
        });

        maleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setMaleFilterOn(true); }
                else { data.setMaleFilterOn(false); }
            }
        });

        femaleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { data.setFemaleFilterOn(true); }
                else { data.setFemaleFilterOn(false); }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                data.setFinish(true);
            }
        });


    }
}