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
import android.util.Log;
import android.widget.Toast;
import com.Blood.types.View.Adapter.AdapterDoctor;
import com.Blood.types.Model.Doctor;
import com.Blood.types.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class DoctorActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_doctor);
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


        showData();
    }



    private void searchNameLine(CharSequence charSequence) {
        ArrayList<Doctor> doctors1 = new ArrayList<>();

        for (Doctor d: doctors ) {

            if (d.getName().contains(charSequence)){
                doctors1.add(d);
            }
        }
        AdapterDoctor ad = new AdapterDoctor(DoctorActivity.this,doctors1);
        recyclerView.setAdapter(ad);


    }



    private void showData()
    {
        db.collection("Doctor").whereEqualTo("bool" ,true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(
                                    DoctorActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("ERRORDOCTOR",error.getMessage());
                            return;
                        }else {
                            for (DocumentChange document: value.getDocumentChanges()) {
                                if (document.getType() == DocumentChange.Type.ADDED){
                                    doctors.add(document.getDocument().toObject(Doctor.class));
                                }
                            }
                            adapterDoctor = new AdapterDoctor(DoctorActivity.this,doctors);
                            recyclerView.setAdapter(adapterDoctor);
                        }
                    }
                }
                );
    }
}