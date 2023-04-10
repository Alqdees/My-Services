package com.Blood.types.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Model> models;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton edit;
    private FirebaseFirestore db;

    private Intent intent;
    private String Types;
    private String [] types;
    private ActionBar actionBar;
    private FirebaseAuth mAuth;
    private String mVerificationId,number,realNumber,name,location,type;

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
        types = new String[]{
                "A+",
                "B+",
                "A-",
                "B-",
                "O+",
                "O-",
                "AB+",
                "AB-",
                "لا أعرف"
        };
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
        mAuth.setLanguageCode("en");
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
                builder.setView(view);
                AlertDialog dialog = builder.create();


                search.setOnClickListener((View view2) ->{
                    number = edit.getText().toString().trim();
                    getNumberUser(number);

//
//                    sendVerificationCode(number);
                    dialog.dismiss();

                });
              dialog.show();
            }
        });

    }

    private void getNumberUser(String nb) {

        for (String s : types) {
//            DocumentReference docRef = db.collection(s).document();

            CollectionReference collectionRef = db.collection(s);
            collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (Objects.equals(document.getString("number"), nb)) {
                                if (nb.charAt(0) == '0'){
                                    name = document.getString("name");
                                    location = document.getString("location");
                                    type =document.getString("type");
                                    realNumber = nb.substring(1);
                                    sendVerificationCode(realNumber);
                                    break;
                                }
                            }
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Some Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("onFailure", "onFailure: "+e.getMessage());
                }
            });
        }

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback is invoked when the verification is complete automatically
                    // You can also use the credential to sign in the user
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {


                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                                MainActivity.this,
                                ""+e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        // Invalid request
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        Toast.makeText(
                                MainActivity.this,
                                ""+e.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        // The SMS quota for the project has been exceeded
                    } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                        Toast.makeText(
                                MainActivity.this,
                                ""+e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        // reCAPTCHA verification attempted with null Activity
                    }
                    // This callback is invoked if an error occurred during the verification process
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken
                                               token)
                {

                    Intent intent=new Intent(getApplicationContext(),OtpActivity.class);
                    intent.putExtra("number",realNumber);
                    intent.putExtra("name",name);
                    intent.putExtra("location",location);
                    intent.putExtra("type",type);
                    intent.putExtra("number",number);
                    intent.putExtra("verificationId",verificationId);
                    startActivity(intent);

                }
            };

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+964"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User signed in successfully
                            FirebaseUser user = task.getResult().getUser();
                        }
                    }
                });
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
