package com.example.bus_tracking;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
   EditText username,password;
   Button submit,signUp;
   TextView textView;
   String username_1,password_1;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= findViewById(R.id.username);
        password=findViewById(R.id.password);
        submit=findViewById(R.id.log);
        signUp=findViewById(R.id.button2);
        textView=findViewById(R.id.textView3);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               exist_user();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,signup.class));
            }
        });



    }

//    private void registerUser(){
//
//        email = editTextEmail.getText().toString().trim();
//        password  = editTextPassword.getText().toString().trim();
//
//        if(TextUtils.isEmpty(email)){
//            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        //displaying a progress dialog
//
//        progressDialog.setMessage("Registering Please Wait...");
//        progressDialog.show();
//
//        //creating a new user
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Overri
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                //checking if success
//                if(task.isSuccessful()){
//                    //display some message here
//                    Toast.makeText(MainActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
//
//                    FirebaseUser currentUser= firebaseAuth.getCurrentUser();
//                    updateUI(currentUser);
//
//                    startActivity(new Intent(getApplicationContext(),Crud_Operation.class));
//                }else{
//                    //display some message here
//                    Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
//                    updateUI(null);
//                }
//                progressDialog.dismiss();
//            }
//        });
//
//    }




    public void exist_user(){

        username_1 = username.getText().toString().trim();
        password_1 = password.getText().toString().trim();

        if(TextUtils.isEmpty(username_1)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password_1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username_1,password_1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(login.this,"Successfully Login",Toast.LENGTH_LONG).show();

                            startActivity(new Intent(getApplicationContext(),dashboard.class));

                            FirebaseUser currentUser= firebaseAuth.getCurrentUser();


                        }else{
                            Toast.makeText(login.this,"Login Error",Toast.LENGTH_LONG).show();



                        }
                        progressDialog.dismiss();
                    }
                });


    }

}