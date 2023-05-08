package com.Blood.types.View.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SelectActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private String newVersion;
    private MotionButton line,blood,doctor ,professions;
    private FloatingActionButton floatingActionButton;
    private FirebaseRemoteConfig remoteConfig;
    private int currentVersionCod;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_select);
        actionBar = getSupportActionBar();
        actionBar.hide();
        db = FirebaseFirestore.getInstance();

        professions = findViewById(R.id.professions);
        line = findViewById(R.id.lineTravel);
        blood = findViewById(R.id.bloods);
        doctor = findViewById(R.id.doctor);
        floatingActionButton=findViewById(R.id.Add);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                    SelectActivity.this,
                    Transportation_linesActivity.class));
            }
        });

        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this,TypeActivity.class));
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new
                    Intent(
                        SelectActivity.this,DoctorActivity.class));

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
showDialog();

//                CollectionReference collectionRef = db.collection("line");
//                collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                if (Objects.equals(document.getBoolean("bool"),false)) {
////                                    String named = document.getString("name");
//                                    String id = document.getId();
//                                    doc = db.collection("line").document(id);
//                                    doc.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Toast.makeText(SelectActivity.this, "تم الحذف ", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(getApplicationContext(),TypeActivity.class));
//                                                finish();
//                                            }
//                                        }
//
//                                    });
//
//                                }else {
//                                    Toast.makeText(SelectActivity.this, "أسمك موجود", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }else {
//                            Toast.makeText(
//                                SelectActivity.this,
//                                "Some Error",
//                                Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("onFailure", "onFailure: "+e.getMessage());
//                    }
//                });
//



//                showDialog();
            }
        });
        professions.setOnClickListener(view -> {
            startActivity(new
                Intent(
                SelectActivity.this,ProfessionUserActivity.class));
        });


///////// below code to update app in on create
        currentVersionCod = getCurrentVersionCode();
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings
            configSettings = new
            FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(5000)
            .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                //newVersion get number integer from firebase
                if (task.isSuccessful()){

                    newVersion = remoteConfig.getString("update");
                    if (Integer.parseInt(newVersion) > currentVersionCod) {
                        showUpdateDialog();
                    }else {
                        return;
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Error", e.getMessage());
            }
        });


    }


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {

            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (Exception e) {
            Toast.makeText(this,
                    "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        assert packageInfo != null;
        return packageInfo.versionCode;
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update,null,false);
        androidx.appcompat.widget.AppCompatButton update = v.findViewById(R.id.update);
        androidx.appcompat.widget.AppCompatButton cancel  = v.findViewById(R.id.cancel);
        dialog.setView(v);
        Dialog dialog1 = dialog.create();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri =
                        Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.Blood.types");
                Intent toTelegram= new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(toTelegram);
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(getApplicationContext(), "Check internet",Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Toast.makeText(SelectActivity.this,
                        "من الضروري تحديث في وقت اخر",
                        Toast.LENGTH_LONG).show();
            }
        });
        dialog1.show();

        dialog.create();
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_main,null,false);
        MotionButton addLine = v.findViewById(R.id.line_request);
        MotionButton addDoctor = v.findViewById(R.id.doctorAdd);
        MotionButton addBlood = v.findViewById(R.id.add_blood);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MotionButton add_Profession = v.findViewById(R.id.add_Profession);
        builder.setView(v);
        AlertDialog dialog = builder.create();


        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this,LineActivity.class));
                dialog.dismiss();
            }
        });
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(SelectActivity.this,Sendrequest.class));
                dialog.dismiss();
            }
        });
        addBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelectActivity.this,RegisterActivity.class);
//                intent.putExtra("isEditMode", true);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        add_Profession.setOnClickListener(View ->{
            startActivity(new Intent(SelectActivity.this,ProfessionActivity.class));
            dialog.dismiss();
        });

        dialog.show();

    }
}