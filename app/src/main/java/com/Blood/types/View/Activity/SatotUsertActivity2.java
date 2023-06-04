package com.Blood.types.View.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.Blood.types.Model.Profession;
import com.Blood.types.Model.Satota;
import com.Blood.types.R;
import com.Blood.types.View.Adapter.AdapterProfession;
import com.Blood.types.View.Adapter.SatotaAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SatotUsertActivity2 extends AppCompatActivity {
  private FirebaseFirestore db;
  private RecyclerView recyclerView;
  private ArrayList<Satota> satotas;
  private ActionBar actionBar;
  private TextInputEditText et_search;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    actionBar = getSupportActionBar();
    actionBar.hide();
    setContentView(R.layout.activity_satot_usert2);

getOpj();
  }


  private void showData() {

    db.collection(SatotaRegisterActivity.Satota)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
          @Override
          public void onEvent(@Nullable QuerySnapshot value, FirebaseFirestoreException error) {
            if (error != null) {
              Toast.makeText(
                  SatotUsertActivity2.this, error.getMessage(),
                  Toast.LENGTH_SHORT).show();
            } else {
              assert value != null;
              for (DocumentChange document : value.getDocumentChanges()) {
                if (document.getType() == DocumentChange.Type.ADDED) {
                  satotas.add(document.getDocument().toObject(Satota.class));
                  satotas.get(0).getNumber();
                }
              }
              SatotaAdapter adapter = new SatotaAdapter(SatotUsertActivity2.this,satotas);
              recyclerView.setAdapter(adapter);
            }
          }
        });
  }
  private void getOpj() {

    db = FirebaseFirestore.getInstance();

    recyclerView = findViewById(R.id.recycler);
    db = FirebaseFirestore.getInstance();
    satotas = new ArrayList<>();
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
    ArrayList<Satota> satota = new ArrayList<>();

    for (Satota s: satota ) {

      if (s.getName().contains(charSequence)){
        satotas.add(s);
      }
    }
    SatotaAdapter ad = new SatotaAdapter(SatotUsertActivity2.this,satotas);
    recyclerView.setAdapter(ad);


  }
}