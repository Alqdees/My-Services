package com.Blood.types.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.Blood.types.Controller.Services;
import com.Blood.types.Model.Model;
import com.Blood.types.Model.Profession;
import com.Blood.types.R;
import com.Blood.types.View.Adapter.AdapterProfession;
import com.Blood.types.View.Adapter.RecyclerViewAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfessionUserActivity extends AppCompatActivity {
  private FirebaseFirestore db;
  private RecyclerView recyclerView;
  private ArrayList<Profession> profession;
  private ActionBar actionBar;
  private TextInputEditText et_search;
  @SuppressLint("MissingInflatedId")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profession_user);
    getOpj();

  }

  private void showData() {

    db.collection(Services.professions.name()).orderBy("name", Query.Direction.ASCENDING)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
          @Override
          public void onEvent(@Nullable QuerySnapshot value, FirebaseFirestoreException error) {
            if (error != null) {
              Toast.makeText(
                  ProfessionUserActivity.this, error.getMessage(),
                  Toast.LENGTH_SHORT).show();
            } else {
              assert value != null;
              for (DocumentChange document : value.getDocumentChanges()) {
                if (document.getType() == DocumentChange.Type.ADDED) {
                  profession.add(document.getDocument().toObject(Profession.class));
                }
              }
              AdapterProfession adapter = new AdapterProfession(profession,ProfessionUserActivity.this);
              recyclerView.setAdapter(adapter);
            }
          }
        });


  }

  private void getOpj() {

    db = FirebaseFirestore.getInstance();

    recyclerView = findViewById(R.id.recycler);
    db = FirebaseFirestore.getInstance();
    profession = new ArrayList<>();
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
    showData();
  }

  private void searchNameLine(CharSequence charSequence) {
    ArrayList<Profession> profession1 = new ArrayList<>();

    for (Profession p: profession ) {

      if (p.getName().contains(charSequence)){
        profession1.add(p);
      }
    }
    AdapterProfession ad = new AdapterProfession(profession1,ProfessionUserActivity.this);
    recyclerView.setAdapter(ad);


  }
}