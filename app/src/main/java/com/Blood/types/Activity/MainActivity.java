package com.Blood.types.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.constraintlayout.utils.widget.MotionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Blood.types.Adapter.RecyclerViewAdapter;
import com.Blood.types.Model.Model;
import com.Blood.types.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    private ArrayList<Model> models;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton edit;
    private FirebaseFirestore db;

    private Intent intent;
    private String Types;
    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private String mVerificationId,mResendToken;

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
        mAuth = FirebaseAuth.getInstance();
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
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_setnumber,null,false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                com.google.android.material.textfield.TextInputEditText
                        edit = view.findViewById(R.id.number);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                MotionButton search = view.findViewById(R.id.searchNumber);
                search.setOnClickListener((View view2) ->{
                    String number = edit.getText().toString();

                    Toast.makeText(MainActivity.this,
                            ""+number,
                            Toast.LENGTH_SHORT).show();
                });
                builder.setView(view);
                builder.create().show();

//                intent = new Intent(
//                        MainActivity.this,RegisterActivity.class);
//
//                intent.putExtra("isEditMode",true);
//                intent.putExtra("types",Types);
//                startActivity(intent);
            }
        });




    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback is invoked when the verification is complete automatically
                    // You can also use the credential to sign in the user
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked if an error occurred during the verification process
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    // This callback is invoked when the verification code is successfully sent to the user's phone number
                    // Save the verification ID and resending token so you can use them later
                    mVerificationId = verificationId;
//                    mResendToken = token;
                }
            };



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
                    public void onEvent(@Nullable QuerySnapshot value,  FirebaseFirestoreException error) {
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
