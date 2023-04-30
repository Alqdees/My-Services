package com.Blood.types.View.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class Sendrequest extends AppCompatActivity {

    private TextInputEditText nameET,numberET,
            specializationET,timeET,titleET;
    private MotionButton sendRequest;
    private FirebaseFirestore db;
    private HashMap<String,Object> doctors;
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

        sendRequest.setOnClickListener(v -> {
            sendRequestDoctor();
        });
    }


    private void sendRequestDoctor(){
        String name = nameET.getText().toString();
        String number = numberET.getText().toString();
        String specialization = specializationET.getText().toString();
        String time = timeET.getText().toString();
        String title = titleET.getText().toString();

        doctors = new HashMap<>();
        doctors.put("name",name);
        doctors.put("number",number);
        doctors.put("presence",time);
        doctors.put("specialization",specialization);
        doctors.put("title",title);
        doctors.put("bool",false);

        db.collection(Doctor).document(number)
            .set(doctors).
            addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(
                        Sendrequest.this,
                        R.string.register_done, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(
                        Sendrequest.this, SelectActivity.class
                    ));
                    finish();
                }
            }).addOnFailureListener(e -> {
                // wait some minute
                Log.d("EXCEPTIONFire",
                    e.getMessage());
            });
    }
}