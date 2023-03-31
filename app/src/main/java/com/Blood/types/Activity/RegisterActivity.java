package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


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
    private String name;
    private String number;
    private String type;
    private String location;
    private String deviceId;
    private String Type;
    private String[] types;
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

                                            // is true register user ...

                                            Toast.makeText(
                                                    RegisterActivity.this
                                                    , "Done",
                                                    Toast.LENGTH_SHORT).show();
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
                                }
                            }

                        });

                    }

                }

            }
        });

    }

}