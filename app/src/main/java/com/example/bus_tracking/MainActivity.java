package com.example.bus_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent inte =new Intent(getApplicationContext(), type_selection.class) ;
                startActivity(inte);
                finish();
            }
        },3000);
    }
}