package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
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
            ET_name,ET_number, ET_location,Et_otp;
    private FirebaseFirestore db;
    private ActionBar actionBar;
    AutoCompleteTextView autoCompleteTextView;
    private androidx.constraintlayout.utils.widget.MotionButton register,deleted,confirm;
    private String name;
    private String number;
    private String type;
    private String location;
    private String deviceId;
    private String Type;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private FloatingActionButton deleted_f;
    private String[] types;
    private String VerificationID;

    private Map<String, Object> users;
    private Intent intent;
    private boolean isEditMode;
    private TextView textView;
    private DocumentReference doc;

    private String [] bloods;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//
//        FirebaseAuth.getInstance().getFirebaseAuthSettings()
//                .setAppVerificationDisabledForTesting(true);


//        String documentReference = db.collection(type).document(deviceId).getId();
        // not change color in dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        db = FirebaseFirestore.getInstance();

        actionBar=getSupportActionBar();
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        actionBar.hide();
        intent= getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode",false);
        Type = intent.getStringExtra("types");
        register = findViewById(R.id.register);
        ET_name = findViewById(R.id.name);
        ET_number = findViewById(R.id.number);
        ET_location = findViewById(R.id.location);
        deleted = findViewById(R.id.delete);
        textView = findViewById(R.id.tv_information);
        textView.setVisibility(View.GONE);
        deleted.setVisibility(View.GONE);
//        confirm = findViewById(R.id.confirm);
//        confirm.setVisibility(View.GONE);
//        Et_otp = findViewById(R.id.OTP_ET);
//        Et_otp.setVisibility(View.GONE);
//


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
//        auth.setLanguageCode("EN");
        if (isEditMode){
//            getDataFirestore()
           updateAndgetData();
        }else {
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    name = ET_name.getText().toString();
                    number = ET_number.getText().toString();
                    type = autoCompleteTextView.getText().toString();
                    location = ET_location.getText().toString();
                    if (TextUtils.isEmpty(name) ||
                            TextUtils.isEmpty(type)
                            || TextUtils.isEmpty(location) || number.isEmpty() ) {

                        Toast.makeText(RegisterActivity.this,
                                "أحد الحقول فارغ", Toast.LENGTH_SHORT).show();

                    }else if (number.length() < 11){
                        Toast.makeText(
                                RegisterActivity.this,
                                "الرقم قصير", Toast.LENGTH_LONG).show();
                    }

                    // this is to register user blood donation
                    else {

                        setData(name,number,type,location);
//                        Et_otp.setVisibility(View.VISIBLE);
//                        confirm.setVisibility(View.VISIBLE);
//                        sendVerifictionCode(ET_number.getText().toString().trim());
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

        confirm = findViewById(R.id.confirm);
        Et_otp = findViewById(R.id.OTP_ET);
        Et_otp.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        /////////////////////////////////////
//        deviceId = Add a new document with a constants Android ID



//         initialize variable

//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String codOtp = Et_otp.getText().toString();
//                if (TextUtils.isEmpty(codOtp)){
//                    Toast.makeText(RegisterActivity.this,
//                    "حقل التأكيد فارغ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }



    public void setData(String name, String number, String type, String location){

        users = new HashMap<>();
        users.put("name", name);
        users.put("number", number);
        users.put("type", type);
        users.put("location", location);
        if(type.equals("لا أعرف")){
            youDoNotKnow();
        }else {

            DocumentReference docID = db.collection(type).document(deviceId);

            docID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    //Here to check if user device id is exist in fire store or not
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                               Toast.makeText(RegisterActivity.this,
                                       "انت مسجل بالفعل", Toast.LENGTH_SHORT).show();
                        } else {

                            // if not exist
                            db.collection(type)
                                    .document(deviceId).set(users).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
//                                          sendVerifictionCode(number);
                                            Toast.makeText(RegisterActivity.this, "Done", Toast.LENGTH_SHORT).show();
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
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

    }

    private void youDoNotKnow() {

        DocumentReference docID = db.collection("none").document(deviceId);
        docID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                //Here to check if user device id is exist in fire store or not
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //if exist
                        Toast.makeText(RegisterActivity.this,
                                "أنت مسجل بالفعل", Toast.LENGTH_SHORT).show();
                    } else {
                        // if not exist
                        db.collection("none")
                                .document(deviceId).set(users).
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
        if (type.isEmpty()) {
            return;
        }

        db.collection(tp).document(deviceId).update(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "تم التحديث ", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }

    private void updateAndgetData(){
        actionBar.setTitle("تحديث المعلومات");
        register.setText("تحديث");
        deleted.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

//        String st = autoCompleteTextView.getText().toString();
        for (int i =0 ;i<types.length;i++){
                DocumentReference docRef = db.collection(types[i]).document(deviceId);
            String id = docRef.getId();
//            Log.d("deviceId",id);
            if (deviceId.equals(id)) {
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    ET_name.setText(document.getString("name"));
                                    ET_number.setText(document.getString("number"));
                                    autoCompleteTextView.setText(document.getString("type"));
                                    ET_location.setText(document.getString("location"));
                                    return;
                                }else if (task.isCanceled()){
                                    Toast.makeText(RegisterActivity.this, "غير موجود", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                      return;
                            }

                        }

                    });
                }else {
                    Toast.makeText(this, "غير مسجل", Toast.LENGTH_SHORT).show();

                    break;
                }
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0 ;i<bloods.length;i++) {
                    switch (bloods[i]) {
                        case "A+":
                            upDateProfile("A+");
                            break;
                        case "A-":
                            upDateProfile("A-");
                            break;
                        case "B+":
                            upDateProfile("B+");
                            break;
                        case "B-":
                            upDateProfile("B-");
                            break;
                        case "AB+":
                            upDateProfile("AB+");
                            break;

                        case "AB-":
                            upDateProfile("AB-");
                            break;

                        case "O+":
                            upDateProfile("O+");
                            break;

                        case "O-":
                            upDateProfile("O-");
                            break;

                        default:
                            Toast.makeText(RegisterActivity.this,
                                    "أسمك غير موجود", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }
        });
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = autoCompleteTextView.getText().toString();
                for (int i = 0;i<types.length;i++){

                    if (types[i].equals(st)) {
                        doc = db.collection(types[i]).document(deviceId);
                        doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "تم الحذف ", Toast.LENGTH_SHORT).show();
                                    finish();
//                        onBackPressed();
                                }
                            }

                        });

                    }

                }

            }
        });

    }


//    public PhoneAuthProvider.OnVerificationStateChangedCallbacks  mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if (code != null){
//                verified(code);
//                Log.d("CODESEND",code);
//                Log.d("CODESEND", "onVerificationCompleted:" + phoneAuthCredential.getSmsCode());
//
////                signInbyCredential(phoneAuthCredential);
//
//            }
//        }
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.d("ERROR",e.getMessage());
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s,
//                @NonNull PhoneAuthProvider.ForceResendingToken token) {
//            super.onCodeSent(s, token);
//            VerificationID = s;
//            forceResendingToken = token;
//        }
//    };

//
//    private void verified(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(this.VerificationID,code);
//        signInbyCredential(credential);
//
//    }
//
//    private void signInbyCredential(PhoneAuthCredential credential) {
//
//
//        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    FirebaseUser user = task.getResult().getUser();
//                    Et_otp.setText(user.toString());
//                    Toast.makeText(RegisterActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                    setData(name,number,type,location);
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("ERROR",e.getMessage());
//            }
//        });
//
//    }
//
//    private void sendVerifictionCode(String number) {
//        String getnumber= number.replaceFirst(0+"","");
//        getnumber = "+964"+getnumber;
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(getnumber)
//                        .setTimeout(60L, TimeUnit.SECONDS)
//                        .setActivity(this)
//                        .setCallbacks(mCallbacks)
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//        Log.d("TAG",getnumber);
//    }
//

}