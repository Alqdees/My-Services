package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;

import java.util.concurrent.TimeUnit;

public class TypeActivity extends AppCompatActivity {


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
    private static final String type = "type";

    private ActionBar actionBar;
    private Button AddDonation,Edit;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        actionBar=getSupportActionBar();
        actionBar.hide();
        initialization();

        intent = new Intent(this,MainActivity.class);
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
                startActivity(new Intent(TypeActivity.this,RegisterActivity.class));
            }
        });

//        findViewById(R.id.edit).setOnClickListener((View v) ->{
//
//            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            String[] options = {"تعديل","الغاء"};
//            dialog.setMessage("أدخل الرقم");
//            EditText editText = new EditText(this);
//            editText.setHint("ادخل رقم الهاتف ...");
//            editText.setInputType(InputType.TYPE_CLASS_PHONE);
//            dialog.setView(editText);
//
//           dialog.setPositiveButton("تعديل", new DialogInterface.OnClickListener() {
//               @Override
//               public void onClick(DialogInterface dialog, int which) {
//                   editData(editText.getText().toString());
//               }
//           });
//           dialog.setPositiveButton("الغاء", new DialogInterface.OnClickListener() {
//               @Override
//               public void onClick(DialogInterface dialog, int which) {
//              dialog.dismiss();
//               }
//           });
//            dialog.create().show();
//            intent = new Intent(TypeActivity.this,RegisterActivity.class);
//
//            startActivity(intent);
//        });


//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TypeActivity.this, RegisterActivity.class));
//            }
//        });



    }

//    private void editData(String number) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber("+9647812591236")
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    @Override
//                    public void onCodeSent(@NonNull String verificationId,
//                                           @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                        // Save the verification id somewhere
//                        // ...
//                        Log.d("onVerificationFailed", "onVerificationFailed: " + verificationId);
//                        // The corresponding whitelisted code above should be used to complete sign-in.
////                        TypeActivity.this.enableUserManuallyInputCode();
//                    }
//
//                    @Override
//                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                        // Sign in with the credential
//                        // ...
//                        signInWithPhoneAuthCredential(phoneAuthCredential);
//                    }
//
//                    @Override
//                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                        // ...
//                        Log.d("onVerificationFailed", "onVerificationFailed: " + e.getMessage());
//                    }
//                })
//                .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//
//
//
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @SuppressLint("LongLogTag")
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("signInWithCredential:success", "signInWithCredential:success");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // Update UI
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w("signInWithCredential:failure", "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                            }
//                        }
//                    }
//                });

//    }
    private void sendMainActivity(String s) {

        intent.putExtra(type,s);
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