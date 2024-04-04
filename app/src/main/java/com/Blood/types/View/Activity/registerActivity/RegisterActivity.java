package com.Blood.types.View.Activity.registerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.Blood.types.R;
import com.Blood.types.View.Activity.OtpActivity;
import com.Blood.types.View.Activity.TypeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class RegisterActivity extends AppCompatActivity {
    private static final String A_PLUS = "A+";
    private static final String A_MINUS = "A-";
    private static final String B_PLUS = "B+";
    private static final String B_MINUS = "B-";
    private static final String AB_PLUS = "AB+";
    private static final String AB_MINUS = "AB-";
    private static final String O_PLUS = "O+";
    private static final String O_MINUS = "O-";

    private com.google.android.material.textfield.TextInputEditText
            ET_name,ET_number, ET_location;
    private FirebaseFirestore db;
    private ActionBar actionBar;
    private AutoCompleteTextView autoCompleteTextView;
    private androidx.constraintlayout.utils.widget.MotionButton register,deleted;
    private String name,nname,nnumber,llocation,ttype, number,type,location,realNumber;


    private FirebaseAuth mAuth;
    private String Type;
    private String[] types;
    private Map<String, Object> users;
    private Intent intent;
    private boolean isEditMode;
    private TextView textView;
    private DocumentReference doc;
    private ProgressBar progressBar;

    private String [] bloods;

    @SuppressLint({"HardwareIds", "MissingInflatedId"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // not change color in dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        db = FirebaseFirestore.getInstance();

        actionBar=getSupportActionBar();
        actionBar.hide();


        intent= getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);
        Type = intent.getStringExtra("types");
        nname = getIntent().getStringExtra("name");
        nnumber = getIntent().getStringExtra("number");
        llocation = getIntent().getStringExtra("location");
        ttype = getIntent().getStringExtra("type");
        register = findViewById(R.id.register);
        ET_name = findViewById(R.id.name);
        ET_number = findViewById(R.id.number);
        ET_location = findViewById(R.id.location);
        deleted = findViewById(R.id.delete);
        textView = findViewById(R.id.tv_information);
        textView.setVisibility(View.GONE);
        deleted.setVisibility(View.GONE);
        types = new String[]{
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
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this,R.layout.drop_down_item,types);
        autoCompleteTextView = findViewById(R.id.typesAuto);
        autoCompleteTextView.setAdapter(arrayAdapter);
        if (isEditMode){
           updateAndgetData();
        }else {

            mAuth = FirebaseAuth.getInstance();
            mAuth.setLanguageCode("en");
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name = ET_name.getText().toString();
                    number = ET_number.getText().toString();
                    type = autoCompleteTextView.getText().toString();
                    location = ET_location.getText().toString();
                    if (TextUtils.isEmpty(name) ||
                            TextUtils.isEmpty(type)
                            || TextUtils.isEmpty(location) || number.isEmpty())
                    {

                        Toast.makeText(RegisterActivity.this,
                                "أحد الحقول فارغ", Toast.LENGTH_SHORT).show();

                    }else if (number.length() < 11){
                        Toast.makeText(
                                RegisterActivity.this,
                                "الرقم قصير", Toast.LENGTH_LONG).show();
                    }

                    // this is to register user blood donation
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        getNumberUser(number);
                    }
                }
            });
        }
         bloods = new String[]{
                 A_PLUS,A_MINUS,
                 B_PLUS,B_MINUS,
                 AB_PLUS,AB_MINUS,
                 O_PLUS,O_MINUS
         };
        actionBar = getSupportActionBar();
    }
    
    private void getNumberUser(String nb) {

        for (String s : bloods) {
//            DocumentReference docRef = db.collection(s).document();

            CollectionReference collectionRef = db.collection(s);
            collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (!Objects.equals(document.getString("number"), nb)) {
                                if (nb.charAt(0) == '0'){
                                    realNumber = nb.substring(1);
                                    sendVerificationCode(realNumber);
                                    break;
                                }
                            }else {
                                Toast.makeText(RegisterActivity.this, "أسمك موجود", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }else {
                        Toast.makeText(
                            RegisterActivity.this,
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

    }
    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+964"+phoneNumber)         // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
                .setActivity(RegisterActivity.this)                          // (optional) Activity for callback binding
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
                        RegisterActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken
                                       token)
            {

                progressBar.setVisibility(View.INVISIBLE);
                Intent intent=new Intent(RegisterActivity.this, OtpActivity.class);
                intent.putExtra("isRegister",true);
                intent.putExtra("name",name);
                intent.putExtra("location",location);
                intent.putExtra("type",type);
                intent.putExtra("number",number);
                intent.putExtra("verificationId",verificationId);
                startActivity(intent);

            }
        };
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // User signed in successfully
                        FirebaseUser user = task.getResult().getUser();
                    }

                }
            });
    }


    private void youDoNotKnow() {

        DocumentReference docID = db.collection("none").document(number);
        docID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //if exist
                        Toast.makeText(RegisterActivity.this,
                                "أنت مسجل بالفعل", Toast.LENGTH_SHORT).show();
                    } else {
                        // if not exist
                        db.collection("none")
                                .document(number).set(users).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        onBackPressed();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this,
                                                e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // here to error
                    Toast.makeText(getApplicationContext(),
                            task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void upDateProfile(String tp){
        name = ET_name.getText().toString();
        number = ET_number.getText().toString();
        type = autoCompleteTextView.getText().toString();
        location = ET_location.getText().toString();
        users = new HashMap<>();
        users.put("name", name);
        users.put("number", number);
        users.put("type", type);
        users.put("location", location);
        if (type.isEmpty() || name.isEmpty() || number.isEmpty()) {
            return;
        }
        CollectionReference collectionRef = db.collection(tp);

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                       if (Objects.equals(snapshot.getString("number"), number)){
                           String id = snapshot.getId();
                           db.collection(type).document(id).update(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Toast.makeText(
                                               RegisterActivity.this,
                                               "تم التحديث ",
                                               Toast.LENGTH_SHORT).show();
                                      startActivity(new Intent(getApplicationContext(), TypeActivity.class));
                                       finish();
                                   }

                               }
                           });
                       }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // here on failure update;
                Log.d("onFailure...", "onFailure: " +e.getMessage());
            }
        });
    }

    private void updateAndgetData(){
        actionBar.setTitle(R.string.update);
        register.setText(R.string.update);
        deleted.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        ET_name.setText(nname);
        ET_number.setText(nnumber);
        ET_location.setText(llocation);
        autoCompleteTextView.setText(ttype);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upDateProfile(autoCompleteTextView.getText().toString());
            }
        });
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = autoCompleteTextView.getText().toString();
                String nb = ET_number.getText().toString().trim();

                CollectionReference collectionRef = db.collection(st);
                collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snap:task.getResult()) {

                                if (Objects.equals(snap.getString("number"), nb)){
                                    String id = snap.getId();
                                    doc = db.collection(st).document(id);
                                    doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "تم الحذف ", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),TypeActivity.class));
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
    }
}