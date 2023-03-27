package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Sendrequest extends AppCompatActivity {


    private TextInputEditText nameET,numberET,
            specializationET,timeET,titleET;
    private MotionButton sendRequest;
    private DatabaseReference ref;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendrequest);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        nameET = findViewById(R.id.name);
        numberET = findViewById(R.id.number);
        specializationET = findViewById(R.id.specialization);
        timeET = findViewById(R.id.time);
        titleET = findViewById(R.id.title);
        sendRequest= findViewById(R.id.addRequest);
        db = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.
                getInstance().getReferenceFromUrl
                        ("https://blood-types-77ce2-default-rtdb.firebaseio.com/");


        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString().trim();
                String number = numberET.getText().toString();
                String specialization = specializationET.getText().toString();
                String time = timeET.getText().toString();
                String title = titleET.getText().toString();
                @SuppressLint("HardwareIds")
                String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                ref.child("Doctor").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ref.child("Doctor").child(deviceId).child("name").setValue(name);
                        ref.child("Doctor").child(deviceId).child("number").setValue(number);
                        ref.child("Doctor").child(deviceId).child("presence").setValue(time);
                        ref.child("Doctor").child(deviceId).child("specialization").setValue(specialization);
                        ref.child("Doctor").child(deviceId).child("title").setValue(title);
                        Toast.makeText(Sendrequest.this,
                                "تم اراسال الطلب", Toast.LENGTH_SHORT).show();
                        nameET.setText("");
                        numberET.setText("");
                        specializationET.setText("");
                        timeET.setText("");
                        titleET.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(Sendrequest.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });


    }
}