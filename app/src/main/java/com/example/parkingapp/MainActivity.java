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

import com.example.parkingapp.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button Registerbtn,LoginButton;
    private EditText InputPhoneNumber,InputPassword;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private TextView AdminLink, NotAdminLink;
//    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        loadingBar = new ProgressDialog(this);
//        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
//        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        Registerbtn = (Button) findViewById(R.id.btnRegister);
        LoginButton = (Button) findViewById(R.id.loginButton);

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

//        AdminLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginButton.setText("Login Admin");
//                AdminLink.setVisibility(View.INVISIBLE);
//                NotAdminLink.setVisibility(View.VISIBLE);
//                parentDbName = "Admins";
//            }
//        });
//
//        NotAdminLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginButton.setText("Login ");
//                AdminLink.setVisibility(View.VISIBLE);
//                NotAdminLink.setVisibility(View.INVISIBLE);
//                parentDbName = "Users";
//
//            }
//        });

    }

    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your phone number...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password..",Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Login to your Account");
            loadingBar.setMessage("Please wait,while we are checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

//        if(chkBoxRememberMe.isChecked())
//        {
//            Paper.book().write(Prevalent.UserPhoneKey, phone);
//            Paper.book().write(Prevalent.UserPasswordKey, password);
//        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName ).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    //retrieve data using getter and setter

                    if (usersData.getPhone().equals(phone)) {
                        //if phone number is correct check the password
                        if (usersData.getPassword().equals(password)) {
                            //allow user access to his account

                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(MainActivity.this,"Welcome Admin,You have logged in successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                //redirect user to home activity after login
                                Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                                startActivity(intent);

                            }else if (parentDbName.equals("Users")){

                                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                //redirect user to home activity after login
                                Intent intent = new Intent(MainActivity.this,ParkActivity.class);
//                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                            }

                        }
                    }else{
                        Toast.makeText(MainActivity.this,"Password is incorrect.",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Account with"+ phone + "number do not exist.",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Toast.makeText(MainActivity.this,"You need to create a new account",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }
}
