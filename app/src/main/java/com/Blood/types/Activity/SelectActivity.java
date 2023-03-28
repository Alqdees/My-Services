package com.Blood.types.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SelectActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private MotionButton line,blood,doctor;
//    private MeowBottomNavigation navigation;
    private FloatingActionButton floatingActionButton;
    private FirebaseRemoteConfig remoteConfig;
    private int currentVersionCod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_select);
        actionBar = getSupportActionBar();
        actionBar.hide();
        line = findViewById(R.id.lineTravel);
        blood = findViewById(R.id.blood);
        doctor = findViewById(R.id.doctor);
        floatingActionButton=findViewById(R.id.Add);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this,Transportation_linesActivity.class));
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

                startActivity(new Intent(SelectActivity.this,DoctorActivity.class));

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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


    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {

            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (Exception e) {
            Toast.makeText(this,
                    "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return packageInfo.versionCode;
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
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.Blood.types");
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
        builder.setView(v);
        AlertDialog dialog = builder.show();


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
                startActivity(new Intent(SelectActivity.this,RegisterActivity.class));
                dialog.dismiss();
            }
        });

        dialog.create();

    }
}