package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TypeActivity extends AppCompatActivity {

    private String mVerificationId,number,realNumber,name,location,type;

    private FirebaseFirestore db;

    private MaterialButton A_plus,
            A_Minus,
            B_plus,
            B_Minus,
            AB_plus,
            AB_Minus,
            O_Plus,
            O_Minus;
//    private ExtendedFloatingActionButton floatingActionButton;
    private Intent intent;
    private static final String Type = "type";
    private FirebaseAuth mAuth;

    private ActionBar actionBar;
    private Button AddDonation;
    private String [] types;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        actionBar=getSupportActionBar();
        actionBar.hide();
        initialization();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        intent = new Intent(this,MainActivity.class);
        mAuth.setLanguageCode("en");
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



//        floatingActionButton = findViewById(R.id.registerBtn);

        A_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("A+");
            }
        });
        A_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("A-");
            }
        });
        B_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("B+");
            }
        });
        B_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("B-");
            }
        });
        AB_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("AB+");
            }
        });
        AB_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("AB-");
            }
        });
        O_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("O+");
            }
        });
        O_Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainActivity("O-");
            }
        });

        AddDonation = findViewById(R.id.addDonation);
        AddDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TypeActivity.this,RegisterActivity.class);

                startActivity(i);
            }
        });

        findViewById(R.id.edit).setOnClickListener((View v) ->{
            View view = LayoutInflater.from(TypeActivity.this).inflate(
                R.layout.dialog_setnumber,
                null,
                false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                dialog.dismiss();

            });
            dialog.show();

        });


//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TypeActivity.this, RegisterActivity.class));
//            }
//        });



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
                                    Log.d("onComplete", "onComplete: ");
                                    realNumber = nb.substring(1);
                                    sendVerificationCode(realNumber);
                                    break;
                                }
                            }

                        }
                    }else {
                        Toast.makeText(TypeActivity.this, "Some Error", Toast.LENGTH_SHORT).show();
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
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback =
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
                        TypeActivity.this,
                        ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {



                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("name", name);
                    intent.putExtra("location", location);
                    intent.putExtra("type", type);
                    intent.putExtra("number", number);
                    intent.putExtra("isEditMode", true);
                    startActivity(intent);
                    // The SMS quota for the project has been exceeded
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Toast.makeText(
                        TypeActivity.this,
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
    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+964"+phoneNumber)         // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)   // Timeout and unit
                .setActivity(TypeActivity.this)                          // (optional) Activity for callback binding
                // If no activity is passed, reCAPTCHA verification can not be used.
                .setCallbacks(callback)                   // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void sendMainActivity(String s) {

        intent.putExtra(Type,s);
        startActivity(intent);
    }
    private void getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String options[] = {"فيسبوك", "وتساب", "تليكرام"};
        builder.setTitle("أختر للمراسلة");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    getMessenger();
                } else if (i == 1) {
                    onClickWhatsapp();

                } else if (i == 2) {
                    onClickTelegram();
                }
            }
        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custemoptions, menu);

        //this code means hidden icon search in this Activity (~_^)
        MenuItem menuItem = menu.findItem(R.id.search);
        menuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.developers){
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
    private void initialization() {
//        mAuth = FirebaseAuth.getInstance();
        A_plus = findViewById(R.id.Aplus);
        A_Minus = findViewById(R.id.Aminus);
        B_plus = findViewById(R.id.Bplus);
        B_Minus = findViewById(R.id.Bminus);
        AB_plus = findViewById(R.id.ABpluse);
        AB_Minus = findViewById(R.id.ABminuse);
        O_Plus = findViewById(R.id.Opluse);
        O_Minus = findViewById(R.id.Ominuse);
    }

    @SuppressLint("HardwareIds")
    private void getMessenger() {
        Uri uri = Uri.parse("fb-messenger://user/100002612665292");

        Intent toMessenger= new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(toMessenger);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), "Please Install Facebook Messenger",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint({"IntentReset", "HardwareIds"})
    private void onClickTelegram() {

        Uri uri = Uri.parse("https://t.me/Alqdees");
        Intent toTelegram= new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(toTelegram);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), "Please Install Telegram",Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("HardwareIds")
    private void onClickWhatsapp() {

        try {
            Intent waIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/9647812591236?text="));
            startActivity(waIntent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }

    }

}