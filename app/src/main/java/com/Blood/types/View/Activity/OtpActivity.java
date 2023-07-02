package com.Blood.types.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.Blood.types.Controller.Services;
import com.Blood.types.R;
import com.Blood.types.View.Activity.registerActivity.ProfessionActivity;
import com.Blood.types.View.Activity.registerActivity.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.HashMap;
import java.util.Objects;

public class OtpActivity extends AppCompatActivity {

    private EditText et1;
    private MotionButton btnsubmit;
    private String realNumber,collection,title,presence,
        specialization,nameProfession,from,time,
        verificationId,name,location,type,number,profession;
    private  ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ActionBar actionBar;
    private boolean isSatotaUpDate,isProfessions,
        isRegister,isProfessionsUpdate,isSatota,
        isLine,isDoctor,isType;
    private FirebaseFirestore db;
    private HashMap<String,Object> professions;
    private HashMap<String,Object> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getObj();

        btnsubmit.setOnClickListener((View v) ->{
            progressBar.setVisibility(View.VISIBLE);
            if (!verificationId.isEmpty() || verificationId != null|| !verificationId.equals("")){
                String getuserotp = et1.getText().toString() ;
                PhoneAuthCredential phoneAuthCredential =
                        PhoneAuthProvider.getCredential(verificationId, getuserotp);
                progressBar.setVisibility(View.VISIBLE);
                btnsubmit.setVisibility(View.INVISIBLE);

                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                  /*
                                  * لايوجد تاكيد لخطوط النقل والاطباء
                                   */

                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (isRegister) {
                                        registerUser();
                                    }else if (isProfessions){
                                        registerProfessions();
                                    }else if (isSatota){
                                      registerSatota();
                                    } else if (isLine) {
                                      updateLineUser();
                                    } else if (isSatotaUpDate) {
                                      updateSatotaUser();
                                    } else if (isDoctor) {
                                      updateDoctorUser();
                                    } else if (isProfessionsUpdate) {
                                      updateProfessionsUser();
                                    } else {
                                      // here to update Blood Type user
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("name", name);
                                        intent.putExtra("location", location);
                                        intent.putExtra("type", type);
                                        intent.putExtra("number", number);
                                        intent.putExtra("isEditMode", true);
                                        startActivity(intent);
                                    }
                                }
                                else {
                                    btnsubmit.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(OtpActivity.this,
                                        R.string.Error_otp, Toast.LENGTH_SHORT).show();
                                }

                            }

                        });
              }

        });
    }

  private void updateProfessionsUser() {
    progressBar.setVisibility(View.INVISIBLE);
    Intent intent = new Intent(getApplicationContext(), ProfessionActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.putExtra("name", name);
    intent.putExtra("profession", profession);
    intent.putExtra("number", number);
    intent.putExtra("isEditMode", true);
    startActivity(intent);

  }

  private void updateDoctorUser() {
  }

  private void updateSatotaUser() {
  }

  private void updateLineUser() {
      
      
  }

  private void registerSatota() {
    FirebaseMessaging.getInstance().getToken()
        .addOnCompleteListener(task -> {
          if (!task.isSuccessful()) {
            Log.d("initVariable", Objects.requireNonNull(task.getException().getMessage()));
            return;
          }
          users = new HashMap<>();
          users.put("name",name);
          users.put("number",number);
          users.put("location",location);
          users.put("token",task.getResult());
          db.collection(Services.Satota.name()).document(number).set(users).
              addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()){
                  Toast.makeText(
                      OtpActivity.this,
                      R.string.register_done,
                      Toast.LENGTH_LONG).show();
                  startActivity(new Intent(
                      OtpActivity.this, SelectActivity.class
                  ));
                  finish();
                }
              }).addOnFailureListener(e -> {
                // wait some minute
                Log.d("EXCEPTIONFire",
                    e.getMessage());
              });

        });
  }

  private void registerProfessions() {

              FirebaseMessaging.getInstance().getToken()
          .addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
              Log.d("initVariable", Objects.requireNonNull(task.getException().getMessage()));
              return;
            }
            // Get new FCM registration token
              professions = new HashMap<>();
              professions.put("name",name);
              professions.put("number",number);
              professions.put("nameProfession",profession);
              professions.put("token",task.getResult());

              db.collection(Services.professions.name()).document(number).set(professions).
                          addOnCompleteListener(task2 -> {
                             if (task2.isSuccessful()){
                             Toast.makeText(
                              OtpActivity.this,
                              R.string.register_done,
                              Toast.LENGTH_LONG).show();
                              startActivity(new Intent(
                              OtpActivity.this, SelectActivity.class
                          ));
                          finish();
                      }
                  }).addOnFailureListener(e -> {
                      // wait some minute
                      Log.d("EXCEPTIONFire",
                          e.getMessage());
                  });

              // TODO: Store the token in your database or send it to your server
          });

    }

    private void registerUser() {
        users = new HashMap<>();
        users.put("name", name);
        users.put("number", number);
        users.put("type", type);
        users.put("location", location);
        db.collection(type).document(number).set(users).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // is true register user ...
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(
                                                    OtpActivity.this
                                                    , "Done",
                                                    Toast.LENGTH_SHORT).show();

                                            startActivity(new Intent(getApplicationContext(),SelectActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(OtpActivity.this,
                                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


    }

    private void getObj() {
      
        et1 = findViewById(R.id.inputotp1);
        progressBar = findViewById(R.id.prograss);
        btnsubmit = findViewById(R.id.btnsubmit);
        actionBar = getSupportActionBar();
        actionBar.hide();
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        isRegister = getIntent().getBooleanExtra("isRegister",false);
        isSatota = getIntent().getBooleanExtra("isSatota",false);
        isType = getIntent().getBooleanExtra("isType",false);
        isDoctor = getIntent().getBooleanExtra("isDoctor",false);
        isLine = getIntent().getBooleanExtra("isLine",false);
        isProfessionsUpdate = getIntent().getBooleanExtra("isProfessions",false);
        isProfessions = getIntent().getBooleanExtra("prof",false);
        realNumber = getIntent().getStringExtra("realNumber");
        verificationId = getIntent().getStringExtra("verificationId");
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        location = getIntent().getStringExtra("location");
        profession = getIntent().getStringExtra("profession");
        type = getIntent().getStringExtra("type");
        from = getIntent().getStringExtra("from");
        time = getIntent().getStringExtra("time");
        nameProfession = getIntent().getStringExtra("nameProfession");
        title = getIntent().getStringExtra("title");
        presence = getIntent().getStringExtra("presence");
        specialization = getIntent().getStringExtra("specialization");
        collection = getIntent().getStringExtra("collection");
        isSatotaUpDate = getIntent().getBooleanExtra("isSatotaUpDate",false);
        
    }
    
}