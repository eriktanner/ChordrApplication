package com.euna.chordr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Spinner keyPicker, modePicker;
    Button updateButton;
    TextView chordsDisplay;

    Chords chords = new Chords();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFindByViews();
        initializeOnClickListeners();
    }

    private void initializeFindByViews() {
        updateButton = (Button) findViewById(R.id.updateButton);
        chordsDisplay = (TextView) findViewById(R.id.chordsDisplay);

        keyPicker = (Spinner) findViewById(R.id.keyPicker);
        ArrayAdapter<String> keyAdapt = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, chords.getNotes());
        keyPicker.setAdapter(keyAdapt);

        modePicker = (Spinner) findViewById(R.id.modePicker);
        ArrayAdapter<String> modeAdapt = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, chords.getModes());
        modePicker.setAdapter(modeAdapt);
    }

    private void initializeOnClickListeners() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String total = "";
                String key = keyPicker.getSelectedItem().toString();
                String[] chordsInKey;

                switch(modePicker.getSelectedItem().toString()) {
                    case "Major":
                        chordsInKey = chords.getMajChords(key);
                        break;

                    case "Minor":
                        chordsInKey = chords.getMinChords(key);
                        break;

                    default:
                        chordsInKey = chords.getMajChords(key);
                }

                for (int i = 0; i < chordsInKey.length; i++) {
                    total += chordsInKey[i] + " ";
                }

                chordsDisplay.setText(total);
            }
        });
    }
}
