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

public class ProfessionActivity extends AppCompatActivity {
  private FirebaseFirestore db;
  private TextInputEditText nameET,numberET,nameProfession;
  private HashMap<String,Object> professions;
  private String Professions = "professions";
  private MotionButton sendRequest;
  private ActionBar actionBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    actionBar = getSupportActionBar();
    actionBar.hide();
    setContentView(R.layout.activity_profession);
    initVariable();
    sendRequest.setOnClickListener(View->{
           sendRequestProfession();
    });

  }

  private void initVariable() {
    db = FirebaseFirestore.getInstance();
    nameET = findViewById(R.id.name);
    numberET = findViewById(R.id.number);
    nameProfession = findViewById(R.id.profession);
    sendRequest = findViewById(R.id.addRequest);
  }

  private void sendRequestProfession(){
    String name = nameET.getText().toString();
    String number = numberET.getText().toString();
    String Profession = nameProfession.getText().toString();


    professions = new HashMap<>();
    professions.put("name",name);
    professions.put("number",number);
    professions.put("nameProfession",Profession);
    professions.put("bool",false);

    db.collection(Professions).document(number)
        .set(professions).
        addOnCompleteListener(task -> {
          if (task.isSuccessful()){
            Toast.makeText(
                ProfessionActivity.this,
                R.string.register_done,
                Toast.LENGTH_LONG).show();
            startActivity(new Intent(
                ProfessionActivity.this, SelectActivity.class
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