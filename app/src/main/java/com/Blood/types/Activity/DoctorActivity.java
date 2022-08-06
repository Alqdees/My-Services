package com.Blood.types.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.Blood.types.Adapter.AdapterDoctor;
import com.Blood.types.Adapter.Adapter_Transport;
import com.Blood.types.Model.Doctor;
import com.Blood.types.Model.ModelTransport;
import com.Blood.types.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DoctorActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private AdapterDoctor adapterDoctor;
    private ArrayList<Doctor> doctors;
    private ActionBar actionBar;

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


        showData();
    }

    private void showData()
    {

        db.collection("Doctor").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(
                                    DoctorActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
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
                });
    }
}