package com.Blood.types.View.Activity.registerActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.Blood.types.Controller.Services;
import com.Blood.types.R;
import com.Blood.types.View.Activity.SelectActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;

public class Sendrequest extends AppCompatActivity {

    private TextInputEditText nameET,numberET,
            specializationET,timeET,titleET;
    private String name ,number,time,specialization,title;
    private MotionButton sendRequest,delete;
    private TextView tv_title;
    private FirebaseFirestore db;
    private DocumentReference doc;
    private HashMap<String,Object> doctors;
    private Intent intent;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendrequest);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        delete = findViewById(R.id.delete);
        delete.setVisibility(View.INVISIBLE);
        tv_title = findViewById(R.id.text);
        nameET = findViewById(R.id.name);
        numberET = findViewById(R.id.number);
        specializationET = findViewById(R.id.specialization);
        timeET = findViewById(R.id.time);
        titleET = findViewById(R.id.title);
        sendRequest= findViewById(R.id.addRequest);
        db = FirebaseFirestore.getInstance();
        intent= getIntent();
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        time = intent.getStringExtra("presence");
        specialization = intent.getStringExtra("specialization");
        title = intent.getStringExtra("title");
        isUpdate= intent.getBooleanExtra("isEditMode",false);

        if (isUpdate){
            tv_title.setText(R.string.update);
            delete.setVisibility(View.VISIBLE);
            nameET.setText(name);
            numberET.setText(number);
            specializationET.setText(specialization);
            timeET.setText(time);
            titleET.setText(title);
            sendRequest.setText(R.string.update);
            sendRequest.setOnClickListener(View -> {
                updateDoctorInfo();
            });
            delete.setOnClickListener(View ->{
                CollectionReference collectionRef = db.collection(Services.Doctor.name());
                collectionRef.get().addOnCompleteListener(v ->{
                    if (v.isSuccessful()) {
                        for (QueryDocumentSnapshot snap : v.getResult()) {

                            if (Objects.equals(snap.getString("number"), number)) {
                                String id = snap.getId();
                                doc = db.collection(Services.Doctor.name()).document(id);
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
            sendRequest.setOnClickListener(v -> {
                sendRequestDoctor();
            });
        }

    }

    private void updateDoctorInfo()  {
        String named = nameET.getText().toString();
        String numberd = numberET.getText().toString();
        String specializationd = specializationET.getText().toString();
        String timed = timeET.getText().toString();
        String titled = titleET.getText().toString();
        HashMap<String,Object> data = new HashMap<String,Object>();
        CollectionReference p = db.collection(Services.Doctor.name());
        data.put("name",named);
        data.put("number",numberd);
        data.put("presence",timed);
        data.put("specialization",specializationd);
        data.put("title",titled);
        p.document(numberd).update(data).addOnCompleteListener( is -> {
            if( is.isSuccessful()){
                Toast.makeText(Sendrequest.this,
                        "تم التحديث",
                        Toast.LENGTH_SHORT).show();
            }
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
        doctors.put("bool",true);

        db.collection(Services.Doctor.name()).document(number)
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