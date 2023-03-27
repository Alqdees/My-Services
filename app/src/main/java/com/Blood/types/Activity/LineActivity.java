package com.Blood.types.Activity;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LineActivity extends AppCompatActivity {
    private TextInputEditText nameET,numberET,
            typeEt,timeET,fromAndToEt;
    private MotionButton sendRequest;
    private DatabaseReference ref;
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
        ref = FirebaseDatabase.
                getInstance().getReferenceFromUrl
                        ("https://blood-types-77ce2-default-rtdb.firebaseio.com/");

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
                    Toast.makeText(LineActivity.this, "أحد الحقول فارغ", Toast.LENGTH_SHORT).show();
                }else {
                    @SuppressLint("HardwareIds")
                    String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    lines.put("name",name);
                    lines.put("number",number);
                    lines.put("type",type);
                    lines.put("time",time);
                    lines.put("from",from);
                    lines.put("bool",false);



                    db.collection("line")
                            .document().set(lines)
                            .addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LineActivity.this, "Done ..."
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("lines_Exception", e.getMessage());
                                }
                            });

//                    ref.child("line").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            ref.child("line").child(deviceId).child("name").setValue(name);
//                            ref.child("line").child(deviceId).child("number").setValue(number);
//                            ref.child("line").child(deviceId).child("type").setValue(type);
//                            ref.child("line").child(deviceId).child("time").setValue(time);
//                            ref.child("line").child(deviceId).child("title").setValue(from);
//                            Toast.makeText(LineActivity.this,
//                                    "تم اراسال الطلب", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
////                            Snackbar.make(R.layout.cardview,"aaa",Snackbar.LENGTH_SHORT).show();
//                        }
//                    });


                }



            }
        });

    }
}