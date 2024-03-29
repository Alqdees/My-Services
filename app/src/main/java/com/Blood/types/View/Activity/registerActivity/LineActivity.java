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
import com.Blood.types.View.Activity.SelectActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Objects;

public class LineActivity extends AppCompatActivity {
    private TextInputEditText nameET,numberET,
            typeEt,timeET,fromAndToEt;
    private String name,number,type,time,from;
    private MotionButton sendRequest,delete;
    private ActionBar actionBar;
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
                        lines.put("bool",true);
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
}