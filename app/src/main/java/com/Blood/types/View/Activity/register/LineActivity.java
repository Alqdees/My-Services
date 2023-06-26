package com.Blood.types.View.Activity.register;

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
import com.Blood.types.View.Activity.SelectActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LineActivity extends AppCompatActivity {
    private TextInputEditText nameET,numberET,
            typeEt,timeET,fromAndToEt;
    private MotionButton sendRequest;
    private ActionBar actionBar;
    private FirebaseFirestore db;
    private HashMap<String,Object> lines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        actionBar = getSupportActionBar();
        actionBar.hide();


        db = FirebaseFirestore.getInstance();
        nameET = findViewById(R.id.name);
        numberET = findViewById(R.id.number);
        typeEt = findViewById(R.id.type);
        timeET = findViewById(R.id.Time);
        fromAndToEt = findViewById(R.id.fromAndTo);
        sendRequest= findViewById(R.id.addRequest);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lines = new HashMap<>();

                String name = nameET.getText().toString();
                String number = numberET.getText().toString();
                String type = typeEt.getText().toString();
                String time = timeET.getText().toString();
                String from = fromAndToEt.getText().toString();
                if (name.isEmpty() || number.isEmpty() || type.isEmpty() || time.isEmpty() || from.isEmpty() ){
                    Toast.makeText(LineActivity.this, R.string.field_error, Toast.LENGTH_SHORT).show();
                }else {

                    lines.put("name",name);
                    lines.put("number",number);
                    lines.put("type",type);
                    lines.put("time",time);
                    lines.put("from",from);
                    lines.put("bool",false);

                    db.collection(Services.line.name())
                            .document(number).set(lines)
                            .addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(
                                    LineActivity.this,
                                    R.string.register_done, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LineActivity.this, SelectActivity.class));
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("lines_Exception", e.getMessage());
                                }
                            });

                }



            }
        });

    }
}