package com.Blood.types.View.Activity.registerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class LineActivity extends AppCompatActivity {
    private TextInputEditText nameET,numberET,
            typeEt,timeET,fromAndToEt;
    private String name,number,type,time,from;
    private MotionButton sendRequest,delete;
    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Intent intent;
    private boolean isEdit;
    private DocumentReference doc;
    private HashMap<String,Object> lines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        actionBar = getSupportActionBar();
        actionBar.hide();
        intent = getIntent();
        db = FirebaseFirestore.getInstance();
        nameET = findViewById(R.id.name);
        numberET = findViewById(R.id.number);
        typeEt = findViewById(R.id.type);
        timeET = findViewById(R.id.Time);
        delete = findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
        fromAndToEt = findViewById(R.id.fromAndTo);
        sendRequest= findViewById(R.id.addRequest);
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");
        isEdit = intent.getBooleanExtra("isEditMode",false);
        if (isEdit){
            delete.setVisibility(View.VISIBLE);
            name = intent.getStringExtra("name");
            number = intent.getStringExtra("number");
            type = intent.getStringExtra("type");
            time = intent.getStringExtra("time");
            from = intent.getStringExtra("from");

            nameET.setText(name);
            numberET.setText(number);
            typeEt.setText(type);
            timeET.setText(time);
            fromAndToEt.setText(from);
            lines = new HashMap<>();

            /**
             * Hard Code // sendRequest.setText("تحديث");
            * */
            sendRequest.setText("تحديث");
            sendRequest.setOnClickListener(View -> {
                name = nameET.getText().toString();
                number = numberET.getText().toString();
                type = typeEt.getText().toString();
                time = timeET.getText().toString();
                from = fromAndToEt.getText().toString();
                lines.put("name",name);
                lines.put("number",number);
                lines.put("type",type);
                lines.put("time",time);
                lines.put("from",from);
                db.collection("line").document(number).update(lines).addOnCompleteListener(
                        isUpdated ->{
                  if (isUpdated.isSuccessful()) {
                      Toast.makeText(this,
                              "تم التحديث",
                              Toast.LENGTH_SHORT).show();
                  }
         }
                );
            });
            delete.setOnClickListener(View -> {
                CollectionReference collectionRef = db.collection("line");
                collectionRef.get().addOnCompleteListener(v ->{
                    if (v.isSuccessful()) {
                        for (QueryDocumentSnapshot snap : v.getResult()) {

                            if (Objects.equals(snap.getString("number"), number)) {
                                String id = snap.getId();
                                doc = db.collection("line").document(id);
                                doc.delete().addOnCompleteListener( isComplete -> {
                                   if (isComplete.isSuccessful()){
                                       Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
                                       intent = new  Intent(this,SelectActivity.class);
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

            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lines = new HashMap<>();
                     name = nameET.getText().toString();
                     number = numberET.getText().toString();
                     type = typeEt.getText().toString();
                     time = timeET.getText().toString();
                     from = fromAndToEt.getText().toString();
                    if (name.isEmpty() || number.isEmpty() || type.isEmpty() || time.isEmpty() || from.isEmpty() ){
                        Toast.makeText(LineActivity.this, R.string.field_error, Toast.LENGTH_SHORT).show();
                    }else {

                        getNumberUser(number);
//
                    }
                }
            });
        }
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
                                    LineActivity.this,
                                    "أسمك موجود",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }else {
                    Toast.makeText(
                            LineActivity.this,
                            R.string.Error_otp,
                            Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("onFailure", e.getMessage());
//          progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void sendVerificationCode(String realNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+964"+realNumber)         // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
                        .setActivity(LineActivity.this)                          // (optional) Activity for callback binding
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

                    Log.e("onVerificationFailed", "Verification failed " + e.getMessage());

                    Toast.makeText(
                            LineActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken
                                               token)
                {

                    Intent intent=new Intent(LineActivity.this, OtpActivity.class);
                    intent.putExtra("isRegLine",true);
                    intent.putExtra("name",name);
                    intent.putExtra("number",number);
                    intent.putExtra("type",type);
                    intent.putExtra("time",time);
                    intent.putExtra("from",from);
                    intent.putExtra("bool",true);



                    intent.putExtra("verificationId",verificationId);
                    startActivity(intent);

                }
            };
}