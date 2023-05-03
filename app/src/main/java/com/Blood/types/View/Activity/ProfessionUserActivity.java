package com.Blood.types.View.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.Blood.types.Model.Doctor;
import com.Blood.types.R;
import com.Blood.types.View.Adapter.AdapterDoctor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProfessionUserActivity extends AppCompatActivity {
  private FirebaseFirestore db;
  private RecyclerView recyclerView;
  private AdapterDoctor adapterDoctor;
  private ArrayList<Doctor> doctors;
  private ActionBar actionBar;
  private TextInputEditText et_search;
  @SuppressLint("MissingInflatedId")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profession_user);


    recyclerView = findViewById(R.id.recycler);
    db = FirebaseFirestore.getInstance();
    doctors = new ArrayList<>();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.hasFixedSize();
    actionBar = getSupportActionBar();
    actionBar.hide();
    et_search = findViewById(R.id.searchEt);
    et_search.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        searchNameLine(charSequence);


      }

      @Override
      public void afterTextChanged(Editable editable) {
//                searchNameLine(editable.toString());
      }
    });




  }
  private void searchNameLine(CharSequence charSequence) {
    ArrayList<Doctor> doctors1 = new ArrayList<>();

    for (Doctor d: doctors ) {

      if (d.getName().contains(charSequence)){
        doctors1.add(d);
      }
    }
    AdapterDoctor ad = new AdapterDoctor(ProfessionUserActivity.this,doctors1);
    recyclerView.setAdapter(ad);


  }
}