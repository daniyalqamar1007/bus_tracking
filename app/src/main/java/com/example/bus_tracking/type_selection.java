package com.example.bus_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class type_selection extends AppCompatActivity {
  Button driverbut,passengerbut;
        TextView textView;
        ImageButton  driverlogo,passengerlogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
      driverlogo= findViewById(R.id.driver_logo_button);
      driverbut=findViewById(R.id.driver_button);
      passengerlogo=findViewById(R.id.passenger_logo_button);
      passengerbut=findViewById(R.id.passenger_button);
      textView=findViewById(R.id.textView2);

      driverlogo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              textView.setText("Driver");
              startActivity(new Intent(getApplicationContext(),login.class));

          }
      });

        driverbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Driver");

                startActivity(new Intent(getApplicationContext(),login.class));

            }
        });


        passengerlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("PASSENGER");
//                Intent intent=new Intent(getApplicationContext(),login.class);
//                startActivity(intent);

            }
        });

        passengerbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("PASSENGER");
//                Intent intent=new Intent(getApplicationContext(),login.class);
//                startActivity(intent);

            }
        });

    }
}