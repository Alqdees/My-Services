package com.Blood.types.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.Blood.types.R;
import com.Blood.types.View.Activity.OtpActivity;
import com.Blood.types.View.Activity.SelectActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class EditAll {

  private String [] bloodTypes;
  private String realNumber, collection,name,location,type,presence,specialization,title,from,time,nameProfession,number;

  private Context ctx;
  private FirebaseAuth mAuth;
  private FirebaseFirestore db;

  public EditAll(Context context){
    this.ctx = context;
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    mAuth.setLanguageCode("ar");
    bloodTypes = new String[]{
        "A+",
        "B+",
        "A-",
        "B-",
        "O+",
        "O-",
        "AB+",
        "AB-",
        "لا أعرف"
    };

  }
  public void getData(String service, String num) {
    this.number = num;
    if (service.equals(ctx.getString(R.string.blood_type))) {

      for (String s : bloodTypes) {
        CollectionReference collectionRef = db.collection(s);

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (QueryDocumentSnapshot document : task.getResult()) {
                if (Objects.equals(document.getString("number"), num)) {
                  if (num.charAt(0) == '0') {
                    name= document.getString("name");
                    type = document.getString("type");
                    location = document.getString("location");
                    realNumber = num.substring(1);
                    collection =s;
                    sendSMSCode(realNumber);
                    break;
                  }
                }
              }
            } else {
              Toast.makeText(
                  ctx,
                  "Some Error",
                  Toast.LENGTH_SHORT).show();
              ((SelectActivity) ctx).progressBar.setVisibility(View.INVISIBLE);

//          progressBar.setVisibility(View.INVISIBLE);
            }

          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(
                ctx,
                e.getMessage(),
                Toast.LENGTH_SHORT).show();
            ((SelectActivity) ctx).progressBar.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.INVISIBLE);
          }
        });
      }
    } else if (service.equals(ctx.getString(R.string.doctor))) {
      getDataFromService("Doctor", num);
    } else if (service.equals(ctx.getString(R.string.professions))) {
      getDataFromService("professions",num);
    }else if (service.equals(ctx.getString(R.string.internal_transfer))){
      getDataFromService("Satota",num);
    }else if (service.equals(ctx.getString(R.string.transmission_lines))){
      getDataFromService("line",num);
    }


  }
  public void getDataFromService(String service, String num) {

        CollectionReference collectionRef = db.collection(service);
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (QueryDocumentSnapshot document : task.getResult()) {
                if (Objects.equals(document.getString("number"), num)) {
                  if (num.charAt(0) == '0'){
                     realNumber = num.substring(1);
                     // now to check collection,s name
                     switch (service){
                       // here to know name Field in document FireStore ...
                       case "Doctor":
                         name =document.getString("name");
                         title = document.getString("title");
                         presence = document.getString("presence");
                         specialization = document.getString("specialization");
                         break;
                       case "Satota" :
                         name = document.getString("name");
                         location = document.getString("location");

                         break;
                       case "line":
                         name = document.getString("name");
                         from = document.getString("from");
                         time = document.getString("time");
                         type = document.getString("type");
                         break;
                       case "professions":
                         name = document.getString("name");
                         nameProfession = document.getString("nameProfession");

                         break;
                     }
                     collection = service;
                     sendSMSCode(realNumber);
                        break;
                  }
                }
              }
            }else {
              Toast.makeText(
                  ctx,task.getException().getMessage(),
                  Toast.LENGTH_SHORT).show();
              ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);
            }
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(
                ctx,
                e.getMessage(),
                Toast.LENGTH_SHORT).show();
            ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);
          }
        });
  }
  private void sendSMSCode(String realNumber) {
    PhoneAuthOptions options =
        PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+964"+realNumber)             // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)    // Timeout and unit
            .setActivity(((SelectActivity)ctx))           // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(callback)                   // OnVerificationStateChangedCallbacks
            .build();
    PhoneAuthProvider.verifyPhoneNumber(options);

  }
  private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback =
      new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
          ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

          if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(
                ctx,
                ""+e.getMessage(),
                Toast.LENGTH_SHORT).show();
            Log.d("onFailure", "onFailure1: "+e.getMessage());
            // Invalid request
          } else if (e instanceof FirebaseTooManyRequestsException) {
            // is mean user contact Quota
            Toast.makeText(
                ctx,
                ""+e.getMessage(),
                Toast.LENGTH_SHORT).show();

            // The SMS quota for the project has been exceeded
          } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
            Toast.makeText(
                ctx,
                ""+e.getMessage(),
                Toast.LENGTH_SHORT).show();
          }

          ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken
                                   token)
        {
          Intent intent=new Intent(ctx, OtpActivity.class);
          ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);

          for (String s:bloodTypes) {
            if (collection.contains(s)){
              intent.putExtra("name",name);
              intent.putExtra("location",location);
              intent.putExtra("type",type);
              intent.putExtra("isTypeUpData",true);
            }
          }
          switch (collection){
            case "Doctor":
             intent.putExtra("name",name);           // name =document.getString("name");
             intent.putExtra("title",title);
             intent.putExtra("presence",presence);
             intent.putExtra("specialization",specialization);
             intent.putExtra("isDoctor",true);
             break;
            case "Satota":
              intent.putExtra("name",name);           // name =document.getString("name");
              intent.putExtra("location",location);
              intent.putExtra("isSatotaUpDate",true);
              break;
            case "line":
              intent.putExtra("name",name);
              intent.putExtra("from",from);
              intent.putExtra("time",time);
              intent.putExtra("type",type);
              intent.putExtra("isLine",true);
              break;
            case "professions":
              intent.putExtra("name",name);
              intent.putExtra("isProfessions",true);
              intent.putExtra("nameProfession",nameProfession);
              break;
          }
          intent.putExtra("number",number);
          intent.putExtra("collection",collection);
          intent.putExtra("verificationId",verificationId);
          ctx.startActivity(intent);
        }
      };


}
