package com.example.bus_tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bus_tracking.Model.DriveInfoModel;
import com.example.bus_tracking.Utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class signup extends AppCompatActivity {
    EditText password,fname,lname,mobile,email,confirmpass;
    Button get_Account,log;
    String username_1,password_1;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentuser;
    FirebaseDatabase database;
    DatabaseReference databaseReference , listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        fname= findViewById(R.id.fname1);
        lname=findViewById(R.id.lname);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        confirmpass=findViewById(R.id.repass);
        get_Account=findViewById(R.id.editTextPersonName);
        log=findViewById(R.id.log);
        get_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               registerUser();
//                inti();
                startActivity(new Intent(getApplicationContext(),home_Screen.class));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
//                inti();
                startActivity(new Intent(signup.this,login.class));
            }
        });
    }

    private void registerUser(){

        username_1 = email.getText().toString().trim();
        password_1  = password.getText().toString().trim();

        if(TextUtils.isEmpty(username_1)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password_1)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(username_1, password_1)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if(task.isSuccessful()){
                    //display some message here
                    Toast.makeText(signup.this,"Successfully registered",Toast.LENGTH_LONG).show();

                 currentuser= firebaseAuth.getCurrentUser();
//                    updateUI(currentUser);
                    inti();

                }else{
                    //display some message here
                    Toast.makeText(signup.this,"Registration Error",Toast.LENGTH_LONG).show();
//                    updateUI(null);
                }
                progressDialog.dismiss();
            }
        });



    }
    private   void inti(){

        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference(Common.DRIVER_INFO_REFERENCE);
        checkFromFirebase();



    }
    private  void checkFromFirebase(){
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signup.this," Error"+e.getMessage(),Toast.LENGTH_LONG).show();
//
                    }
                }).addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.d("TOKEN" , instanceIdResult.getToken());
                UserUtils.updateToken( signup.this,instanceIdResult.getToken());
            }
        });


//        showREgistration();
        databaseReference.child(currentuser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(getApplicationContext(),"user already Register",Toast.LENGTH_SHORT).show();
                            DriveInfoModel driveInfoModel= snapshot.getValue(DriveInfoModel.class);
                            goToHomeActivity(driveInfoModel);


                        }else
                        {
                            showREgistration();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(signup.this," Error"+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void goToHomeActivity(DriveInfoModel driveInfoModel) {
        Common.currentUser =driveInfoModel;
        startActivity(new Intent(signup.this,home_Screen.class));
        finish();
    }

    private void showREgistration() {
      if(TextUtils.isEmpty(fname.getText().toString())){
          Toast.makeText(getApplicationContext(),"Please enter First Name",Toast.LENGTH_SHORT).show();
            return;

      }else if(TextUtils.isEmpty(lname.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please enter Last Name",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(mobile.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please enter Mobile number",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please enter Email Address",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(password.getText().toString())){
          Toast.makeText(getApplicationContext(),"Please enter First Name",Toast.LENGTH_SHORT).show();
          return;

      }else{

          final DriveInfoModel model=new DriveInfoModel();
          model.setFirstname(fname.getText().toString());
          model.setLastname(lname.getText().toString());
          model.setPhone(mobile.getText().toString());
          model.setEmail(email.getText().toString());
          model.setFirstname(fname.getText().toString());
          model.setPassword(password.getText().toString());
          model.setRating(0.0);
          databaseReference.child(currentuser.getUid()).setValue(model)
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                      }
                  })
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Toast.makeText(getApplicationContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
                          goToHomeActivity(model);

                      }
                  });


      }


    }



}