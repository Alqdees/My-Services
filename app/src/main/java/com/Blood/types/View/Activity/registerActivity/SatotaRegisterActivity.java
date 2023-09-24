package com.Blood.types.View.Activity.registerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.Blood.types.View.Activity.SelectActivity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SatotaRegisterActivity extends AppCompatActivity {

  private TextInputEditText E_name,E_number,E_locationWork;
  private MotionButton addSatota,delete;
  private DocumentReference doc;
  private FirebaseFirestore db;
  private FirebaseAuth mAuth;
  private boolean isEdit;
  private ProgressBar progressBar;
  private Intent intent;
  private String name,number,location;
  private ActionBar actionBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    actionBar = getSupportActionBar();
    actionBar.hide();
    setContentView(R.layout.activity_satota);
    getAllObject();
    delete.setVisibility(View.INVISIBLE);
    intent = getIntent();
    isEdit = intent.getBooleanExtra("isEditMode",false);
    name = intent.getStringExtra("name");
    number = intent.getStringExtra("number");
    location = intent.getStringExtra("location");
    if (isEdit){
      delete.setVisibility(View.VISIBLE);

      E_name.setText(name);
      E_number.setText(number);
      E_locationWork.setText(location);
      addSatota.setText(R.string.update);
      addSatota.setOnClickListener(View -> {
        updateSatota();
      }
      );
      delete.setOnClickListener(View -> {
        CollectionReference collectionRef = db.collection("Satota");
        collectionRef.get().addOnCompleteListener(v ->{
          if (v.isSuccessful()) {
            for (QueryDocumentSnapshot snap : v.getResult()) {

              if (Objects.equals(snap.getString("number"), number)) {
                String id = snap.getId();
                doc = db.collection("Satota").document(id);
                doc.delete().addOnCompleteListener( isComplete -> {
                  if (isComplete.isSuccessful()){
                    Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
                    intent = new  Intent(this, SelectActivity.class);
                    startActivity(intent);
                    finish();
                  }
                });
              }
            }
          }
        });
      });
    }else {
      addSatota.setOnClickListener(View ->{

        name = E_name.getText().toString();
        number = E_number.getText().toString();
        location = E_locationWork.getText().toString();


        if (TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(number)
                || TextUtils.isEmpty(location) || number.isEmpty())
        {

          Toast.makeText(SatotaRegisterActivity.this,
                  "أحد الحقول فارغ", Toast.LENGTH_SHORT).show();
          return;

        }else if (number.length() < 11){
          Toast.makeText(
                  SatotaRegisterActivity.this,
                  "الرقم قصير", Toast.LENGTH_LONG).show();
          return;
        }
        // this is to register user blood donation
        else {
          progressBar.setVisibility(View.VISIBLE);

          getNumberUser(number);
        }
      });
    }


  }

  private void updateSatota() {
    name = "";
    number = "";
    location = "";

    name = E_name.getText().toString();
    number = E_number.getText().toString();
    location = E_locationWork.getText().toString();

    HashMap<String , Object > data = new HashMap<>();

    data.put("name", name);
    data.put("number",number);
    data.put("location", location);
    CollectionReference update = db.collection(Services.Satota.name());
    update.document(number).update(data).addOnCompleteListener( isUpdated ->{
      if (isUpdated.isSuccessful()){
        Toast.makeText(this, "تم التحديث", Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void getNumberUser(String nb) {
    CollectionReference collectionRef = db.collection(Services.Satota.name());
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
                  SatotaRegisterActivity.this,
                  "أسمك موجود",
                  Toast.LENGTH_SHORT).show();
              progressBar.setVisibility(View.INVISIBLE);
              break;
            }
          }
        }else {
          Toast.makeText(
              SatotaRegisterActivity.this,
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
//          progressBar.setVisibility(View.INVISIBLE);
      }
    });


  }
  private void sendVerificationCode(String phoneNumber) {

    PhoneAuthOptions options =
        PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+964"+phoneNumber)         // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
            .setActivity(SatotaRegisterActivity.this)                          // (optional) Activity for callback binding
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
              SatotaRegisterActivity.this,
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
          Intent intent=new Intent(SatotaRegisterActivity.this, OtpActivity.class);
          intent.putExtra("name",name);
          intent.putExtra("number",number);
          intent.putExtra("location",location);
          intent.putExtra(Services.Satota.name(),true);
          intent.putExtra("verificationId",verificationId);
          startActivity(intent);

        }
      };


  private void getAllObject() {

    E_name = findViewById(R.id.Sname);
    E_number = findViewById(R.id.Snumber);
    E_locationWork = findViewById(R.id.Slocation);
    addSatota = findViewById(R.id.addRequest);
    progressBar =findViewById(R.id.Sprogress);
    delete= findViewById(R.id.delete);
    progressBar.setVisibility(View.INVISIBLE);
    db = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    mAuth.setLanguageCode("ar");
  }
}