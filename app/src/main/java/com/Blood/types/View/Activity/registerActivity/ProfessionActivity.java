package com.Blood.types.View.Activity.registerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Blood.types.Controller.Services;
import com.Blood.types.R;
import com.Blood.types.View.Activity.OtpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ProfessionActivity extends AppCompatActivity {
  private FirebaseFirestore db;
  private FirebaseAuth mAuth;
  private TextInputEditText nameET,numberET,nameProfession;

  private String name,number,profession;
  private ProgressBar progressBar;
  private MotionButton sendRequest;
  private ActionBar actionBar;
  private boolean isEditMode;
  private Intent intent;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar = getSupportActionBar();
    actionBar.hide();
    setContentView(R.layout.activity_profession);
    initVariable();
    intent= getIntent();
    isEditMode = intent.getBooleanExtra("isEditMode",false);
    name = intent.getStringExtra("name");
    profession = intent.getStringExtra("profession");
    number = intent.getStringExtra("number");
    if (isEditMode){
      editInfo();
    }else {
        sendRequest.setOnClickListener(View->{
            name = nameET.getText().toString();
            number = numberET.getText().toString();
            profession = nameProfession.getText().toString();


            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(number)
                    || TextUtils.isEmpty(profession) || number.isEmpty())
            {

                Toast.makeText(ProfessionActivity.this,
                        "أحد الحقول فارغ", Toast.LENGTH_SHORT).show();
                return;

            }else if (number.length() < 11){
                Toast.makeText(
                        ProfessionActivity.this,
                        "الرقم قصير", Toast.LENGTH_LONG).show();
                return;
            }

            // this is to register user blood donation
            else {
                progressBar.setVisibility(android.view.View.VISIBLE);
                getNumberUser(number);
            }
        });
    }
  }

  private void editInfo() {
    sendRequest.setText(R.string.update);

    nameET.setText(name);
    nameProfession.setText(profession);
    numberET.setText(number);

    sendRequest.setOnClickListener((View ->{
updateProfession();
    }));

  }

  private void updateProfession() {
      String named = nameET.getText().toString();
      String prof = nameProfession.getText().toString();
      String numberd = numberET.getText().toString();
      HashMap<String,Object> data = new HashMap<String,Object>();
      CollectionReference p = db.collection(Services.professions.name());
      data.put("name",named);
      data.put("nameProfession",prof);
      data.put("number",numberd);
      p.document(numberd).update(data)
              .addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                  Toast.makeText(ProfessionActivity.this, "تم التحديث", Toast.LENGTH_SHORT).show();
              }
          }
      }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(ProfessionActivity.this, "فشل التحديث ", Toast.LENGTH_SHORT).show();
                  }
              });
  }


  private void getNumberUser(String nb) {
      CollectionReference collectionRef = db.collection(Services.professions.name());
      collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {

              if (!Objects.equals(document.getString("number"), nb)) {
                if (nb.charAt(0) == '0'){
                String realNumber = nb.substring(1);
                  sendVerificationCode(realNumber);
                  break;
                }
              }else {
                Toast.makeText(
                    ProfessionActivity.this,
                    "أسمك موجود",
                    Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                break;
              }
            }
          }else {
            Toast.makeText(
                ProfessionActivity.this,
                R.string.Error_otp,
                Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
          }

        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.d("onFailure", e.getMessage());
          progressBar.setVisibility(View.INVISIBLE);
        }
      });
  }

  private void sendVerificationCode(String phoneNumber) {

    PhoneAuthOptions options =
        PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+964"+phoneNumber)         // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
            .setActivity(ProfessionActivity.this)                          // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(mCallbacks)                   // OnVerificationStateChangedCallbacks
            .build();
    PhoneAuthProvider.verifyPhoneNumber(options);
  }

  private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
      new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
//                signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

          progressBar.setVisibility(View.INVISIBLE);

          Toast.makeText(
              ProfessionActivity.this,
              e.getMessage(),
              Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken
                                   token)
        {
//
          progressBar.setVisibility(View.INVISIBLE);
          Intent intent=new Intent(ProfessionActivity.this, OtpActivity.class);
          intent.putExtra("name",name);
          intent.putExtra("number",number);
          intent.putExtra("profession",profession);
          intent.putExtra("prof",true);
          intent.putExtra("verificationId",verificationId);
          startActivity(intent);

        }
      };

  private void initVariable() {

    mAuth = FirebaseAuth.getInstance();
    mAuth.setLanguageCode("ar");
    db = FirebaseFirestore.getInstance();
    nameET = findViewById(R.id.name);
    numberET = findViewById(R.id.number);
    nameProfession = findViewById(R.id.profession);
    sendRequest = findViewById(R.id.addRequest);
    progressBar =findViewById(R.id.progress);
    progressBar.setVisibility(View.GONE);

  }

}