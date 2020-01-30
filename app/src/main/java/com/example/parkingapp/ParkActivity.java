package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ParkActivity extends AppCompatActivity {

    private EditText edName,edVregNo,edPhone,edParking,edHours,edAmount;
    private Button btnPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park);


        //initialize theme here

        edName = (EditText ) findViewById(R.id.edName);
        edVregNo = (EditText ) findViewById(R.id.edVregNo);
        edPhone = (EditText ) findViewById(R.id.edPhone);
        edParking = (EditText )  findViewById(R.id.edParking);
        edHours = (EditText )  findViewById(R.id.edHours);
        edAmount = (EditText )  findViewById(R.id.edAmount);
        btnPark = (Button) findViewById(R.id.btnPark);
    }
}
