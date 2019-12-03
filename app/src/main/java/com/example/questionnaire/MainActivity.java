package com.example.questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static android.nfc.cardemulation.CardEmulation.EXTRA_CATEGORY;
import static android.provider.MediaStore.Video.VideoColumns.CATEGORY;

public class MainActivity extends AppCompatActivity {

    private Button buttonAnimals;

    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireWidgets();
        setListeners();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void wireWidgets() {
        buttonAnimals = findViewById(R.id.button_animal_main);

    }

    private void setListeners() {
        buttonAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create an intent
                Intent targetIntent = new Intent(MainActivity.this, QuizActivity.class);
                // put a string extra with whatever the text of the current button is
                targetIntent.putExtra(EXTRA_CATEGORY,((Button)view).getText().toString());
                startActivity(targetIntent);
                finish();
            }
        });
    }

}
