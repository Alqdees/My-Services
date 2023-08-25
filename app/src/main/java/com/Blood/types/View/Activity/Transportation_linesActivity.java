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
import com.Blood.types.View.Adapter.Adapter_Transport;
import com.Blood.types.Model.ModelTransport;
import com.Blood.types.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class Transportation_linesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<ModelTransport> arrayList;
    private RecyclerView recyclerView;
    private Adapter_Transport adapter;
    private ActionBar actionBar;
    private TextInputEditText et_search;
    @SuppressLint("MissingInflatedId")
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
        ArrayList<ModelTransport> modelTransports = new ArrayList<>();

        for (ModelTransport l: arrayList ) {

            if (l.getName().contains(charSequence)){
                modelTransports.add(l);
            }
        }
       Adapter_Transport ad = new Adapter_Transport(Transportation_linesActivity.this,modelTransports);
        recyclerView.setAdapter(ad);


    }

    private void showData()
    {

        db.collection(Services.line.name()).whereEqualTo("bool",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(
                                    Transportation_linesActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();

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