package com.example.bus_tracking.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.bus_tracking.Common;
import com.example.bus_tracking.Model.TokenModel;
import com.example.bus_tracking.Service.MyFirebaseMassagingService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import androidx.annotation.NonNull;

public class UserUtils {
        public static void updateUser(View view , Map<String,Object> updateDate) {
            FirebaseDatabase.getInstance()
                    .getReference(Common.DRIVER_INFO_REFERENCE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(updateDate)
                    .addOnFailureListener(e -> Snackbar.make(view,e.getMessage(),Snackbar.LENGTH_SHORT).show())
        .addOnSuccessListener(aVoid -> Snackbar.make(view,"update information succesfully !",Snackbar.LENGTH_SHORT).show());



            }


    public static void updateToken(Context context, String token) {

        TokenModel tokenmodel = new TokenModel(token);
        FirebaseDatabase.getInstance ().getReference(Common.TOKEN_REFERENCE)
.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(tokenmodel)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });





















    }
}

