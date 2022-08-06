package com.Blood.types.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.Blood.types.Adapter.Adapter_Transport;
import com.Blood.types.Model.ModelTransport;
import com.Blood.types.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Transportation_linesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<ModelTransport> arrayList;
    private RecyclerView recyclerView;
    private Adapter_Transport adapter;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_lines);
        actionBar= getSupportActionBar();
        actionBar.hide();
        db = FirebaseFirestore.getInstance();

        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

    showData();
    }
    private void showData()
    {

        db.collection("line").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(
                                    Transportation_linesActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            for (DocumentChange document: value.getDocumentChanges()) {
                                if (document.getType() == DocumentChange.Type.ADDED){
                                    arrayList.add(document.getDocument().toObject(ModelTransport.class));
                                }
                            }
                            adapter = new Adapter_Transport(Transportation_linesActivity.this,arrayList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }

}