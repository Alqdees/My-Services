package com.Blood.types.Activity;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Blood.types.Adapter.RecyclerViewAdapter;
import com.Blood.types.Model.Model;
import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    private ArrayList<Model> models;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton edit;
    private FirebaseFirestore db;
    private FirebaseRemoteConfig remoteConfig;
    private int currentVersionCod;
    private Intent intent;
    private String Types;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // not change color in dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /////////
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.hide();

        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        Types = intent.getStringExtra("type");

        //check types in server
        switch (Types){
            case "A+":
                showData("A+");
                break;
            case "A-":
                showData("A-");
                break;
            case "B+":
                showData("B+");
                break;
            case "B-":
                showData("B-");
                break;
            case "AB+":
                showData("AB+");
                break;
            case "AB-":
                showData("AB-");
                break;
            case "O+":
                showData("O+");
                break;
            case "O-":
                showData("O-");
                break;
            default:
        }
        edit = findViewById(R.id.edit_profile);
        models = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
//            showData("A+");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(
                        MainActivity.this,RegisterActivity.class);

                intent.putExtra("isEditMode",true);
                intent.putExtra("types",Types);
                startActivity(intent);
            }
        });



///////// below code to update app in on create
        currentVersionCod = getCurrentVersionCode();
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                //newVersion get number integer from firebase
                final String newVersion = remoteConfig.getString("newVersion");
                if (Integer.parseInt(newVersion) > getCurrentVersionCode()) {
                    showUpdateDialog();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private void showUpdateDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update,null,false);
        com.google.android.material.button.MaterialButton update = v.findViewById(R.id.update);
        com.google.android.material.button.MaterialButton cancel  = v.findViewById(R.id.cancel);
        dialog.setView(v);
        Dialog dialog1 = dialog.create();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://t.me/Blood_Dontion");
                Intent toTelegram= new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(toTelegram);
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(getApplicationContext(), "Please Install Telegram",Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Toast.makeText(MainActivity.this, "من الضروري تحديث في وقت اخر", Toast.LENGTH_SHORT).show();
            }
        });
        dialog1.show();

//        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW
//                            , Uri.parse("")));
//                } catch (Exception e) {
////                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).create().show();
        dialog.create();
//        dialog.setCancelable(false);
    }
    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {

            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return packageInfo.versionCode;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.custemoptions, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchBar(newText);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.developers:
                showDialogIfo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogIfo() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_info,null,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        builder.create().show();
    }

    private void searchBar(String search) {
       ArrayList<Model> list = new ArrayList<>();
        for (Model m: models) {
            if (m.getType().toLowerCase().contains(search.toLowerCase())
                    || m.getName().toLowerCase().contains(search.toLowerCase())){
                list.add(m);
            }
        }
        RecyclerViewAdapter viewAdapter =
                new RecyclerViewAdapter(MainActivity.this,list);
        recyclerView.setAdapter(viewAdapter);
    }
    private void showData(String st)
    {

        db.collection(st).orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Toast.makeText(
                                    MainActivity.this, error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            for (DocumentChange document: value.getDocumentChanges()) {
                                if (document.getType() == DocumentChange.Type.ADDED){
                                    models.add(document.getDocument().toObject(Model.class));
                                }
                            }
                            adapter = new RecyclerViewAdapter(MainActivity.this,models);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }


//    private void speechToText() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.SpeechToText);
//        try {
//            startActivityForResult(intent, SPEECH_REQUEST);
//        } catch (Exception e) {
//            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case SPEECH_REQUEST:
//                if (resultCode == RESULT_OK && data != null) {
//                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    searchBar(result.get(0));
//                }
//        }
//    }

}
