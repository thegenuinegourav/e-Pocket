package com.doomers.hackpaytm;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SetReminderActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageView btnSpeak;
    private EditText description;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        txtSpeechInput = (TextView) findViewById(R.id.textinputSpeech);
        btnSpeak = (ImageView) findViewById(R.id.imageView2);
        description = (EditText)findViewById(R.id.description);

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        // Spinner element
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Today");
        categories.add("Tomorrow");
        categories.add("Day after tomorrow");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);


        // Spinner element
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        // Spinner Drop down elements
        List<String> categories2 = new ArrayList<String>();
        categories2.add("16:00");
        categories2.add("17:00");
        categories2.add("18:00");
        categories2.add("19:00");
        categories2.add("20:00");
        categories2.add("21:00");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner2.setAdapter(dataAdapter2);

        // Spinner element
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);

        // Spinner Drop down elements
        List<String> categories3 = new ArrayList<String>();
        categories3.add("One-Time");
        categories3.add("Two-Times");
        categories3.add("Three-Times");
        categories3.add("Five-Times");
        categories3.add("Ten-Times");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);

        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner3.setAdapter(dataAdapter3);
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    if(txtSpeechInput.getText().toString().equals("yes")) {
                        if (description.getText().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Complete the entries first!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Reminder set successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                        }
                    }
                    else if(txtSpeechInput.getText().toString().equals("no") || txtSpeechInput.getText().toString().equals("cancel"))
                    {
                        Toast.makeText(getApplicationContext(),"Reminder canceled",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    else if(txtSpeechInput.getText().toString().equals("reset"))
                    {
                        Toast.makeText(getApplicationContext(),"Reminder reset",Toast.LENGTH_SHORT).show();
                        description.setText("");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Didn't get it!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }

        }
    }

    }


