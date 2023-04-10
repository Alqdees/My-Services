package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {


    private EditText et1;
    private MotionButton btnsubmit;
    private  String realNumber,verificationId,name,location,type,number;
    private  ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ActionBar actionBar;

    /**
     *  جاي اتحقق من الرمز الي يوصل للمستخدم
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getObj();
        actionBar = getSupportActionBar();
        actionBar.hide();
         realNumber = getIntent().getStringExtra("number");

        verificationId = getIntent().getStringExtra("verificationId");

        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        location = getIntent().getStringExtra("location");
        type = getIntent().getStringExtra("type");


//        textView.setText();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(realNumber, verificationId);

        mAuth = FirebaseAuth.getInstance();

        btnsubmit.setOnClickListener((View v) ->{

            if (!verificationId.isEmpty() || verificationId != null|| !verificationId.equals(null)){
                String getuserotp = et1.getText().toString() ;
                PhoneAuthCredential phoneAuthCredential =
                        PhoneAuthProvider.getCredential(verificationId, getuserotp);
                progressBar.setVisibility(View.VISIBLE);
                btnsubmit.setVisibility(View.INVISIBLE);

                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                btnsubmit.setVisibility(View.VISIBLE);

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("name",name);
                                    intent.putExtra("location",location);
                                    intent.putExtra("type",type);
                                    intent.putExtra("number",number);
                                    intent.putExtra("isEditMode",true);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(OtpActivity.this, R.string.Error_otp, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }

        });
    }

//    private void verifyVerificationCode(String otp) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
//        signInWithPhoneAuthCredential(credential);
//    }


    private void getObj() {

        et1 = findViewById(R.id.inputotp1);
        progressBar = findViewById(R.id.probar2);
        btnsubmit = findViewById(R.id.btnsubmit);

        //get mobile number from mainActivty to this
//        textView = findViewById(R.id.txtmobileno);
    }



//    private void movenumtonext() {
//
//
//
//        et1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (!charSequence.toString().trim().isEmpty()) {
//                    et2.requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        et2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (!charSequence.toString().trim().isEmpty()) {
//                    et3.requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        et3.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (!charSequence.toString().trim().isEmpty()) {
//                    et4.requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        et4.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (!charSequence.toString().trim().isEmpty()) {
//                    et5.requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        et5.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (!charSequence.toString().trim().isEmpty()) {
//                    et6.requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        et6.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//
//    }
}