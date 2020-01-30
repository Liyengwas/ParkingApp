package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingapp.Model.Parking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ParkActivity extends AppCompatActivity {

    private EditText edName,edVregNo,edPhone,edParking,edHours,edAmount;
    private Button btnPark;
    Parking parking;
    private ProgressDialog loadingBar;
    DatabaseReference reff;


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
        loadingBar = new ProgressDialog(this);
        parking = new Parking();
//        reff = FirebaseDatabase.getInstance().getReference().child("Parking");


        //create seton click event listener to send data to the database
        btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParkVehicle();
            }

        });
    }

            private void ParkVehicle() {

                String owner = edName.getText().toString();
                String vehicle = edVregNo.getText().toString();
                String phone = edPhone.getText().toString();
                String parking = edName.getText().toString();
                String hours = edHours.getText().toString();
                String fees = edAmount.getText().toString();


                if (TextUtils.isEmpty(owner)){
                    Toast.makeText(this,"Please write your name...",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(vehicle)){
                    Toast.makeText(this,"Please write your vehicle registration number...",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone)){
                    Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(parking)){
                    Toast.makeText(this,"Please write your parking number...",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(hours)){
                    Toast.makeText(this,"Please write your parking hours...",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(fees)){
                    Toast.makeText(this,"Please write your parking fees...",Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.setTitle("Park Your Vehicle");
                    loadingBar.setMessage("Please wait,while we are processing your request");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();


                    //validate the parking order here
                    Validateparkingrequest(owner,vehicle,phone,parking,hours,fees);
            }
        }

        private void Validateparkingrequest(final String owner, final String vehicle, final String phone, final String parking, final String hours, final String fees) {

            final DatabaseReference RootRef;

            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check if user already exists
                    if(!(dataSnapshot.child("Parking Slots").child("phone").exists())){

                        //if phone doesn't exist create data for new user
                        HashMap<String,Object> userdataMap = new HashMap<>();
                        userdataMap.put("owner",owner);
                        userdataMap.put("vehicle",vehicle);
                        userdataMap.put("phone",phone);
                        userdataMap.put("parking",parking);
                        userdataMap.put("hours",hours);
                        userdataMap.put("fees",fees);

                        RootRef.child("Parking Slots").child(parking).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(ParkActivity.this,"Congratulations,request processed successfully",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    //redirect user to login activity
                                    Intent intent = new Intent(ParkActivity.this,LogoutActivity.class);
                                    startActivity(intent);

                                }else{

                                    loadingBar.dismiss();
                                    Toast.makeText(ParkActivity.this,"Network Error,Please try again some other time",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        //if number already exists tell user that it does
                        Toast.makeText(ParkActivity.this,"This" + parking + "already taken",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(ParkActivity.this,"Please try selecting another parking number",Toast.LENGTH_SHORT).show();

                        //redirect user to the main activity
                        Intent intent = new Intent(ParkActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


        });


    }
}
