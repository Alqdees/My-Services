package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class Sendrequest extends AppCompatActivity {

    private TextInputEditText nameET,numberET,
            specializationET,timeET,titleET;
    private MotionButton sendRequest;
    private FirebaseFirestore db;
    private HashMap<String,Object> doctor;
    private String Doctor = "Doctor";

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

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString().trim();
                String number = numberET.getText().toString();
                String specialization = specializationET.getText().toString();
                String time = timeET.getText().toString();
                String title = titleET.getText().toString();

                doctor = new HashMap<>();
                doctor.put("name",name);
                doctor.put("number",number);
                doctor.put("presence",time);
                doctor.put("specialization",specialization);
                doctor.put("title",title);
                doctor.put("bool",false);

                db.collection(Doctor).document(number)
                        .set(doctor).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(
                                    Sendrequest.this,
                                    "Done", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // wait some minute
                        Log.d("EXCEPTIONFire",
                                e.getMessage());
                    }
                });
            }
        });
    }
}