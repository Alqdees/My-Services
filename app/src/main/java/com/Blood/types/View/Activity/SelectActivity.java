package com.Blood.types.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.Blood.types.Controller.EditAll;
import com.Blood.types.R;
import com.Blood.types.View.Activity.registerActivity.LineActivity;
import com.Blood.types.View.Activity.registerActivity.ProfessionActivity;
import com.Blood.types.View.Activity.registerActivity.RegisterActivity;
import com.Blood.types.View.Activity.registerActivity.SatotaRegisterActivity;
import com.Blood.types.View.Activity.registerActivity.Sendrequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
public class SelectActivity extends AppCompatActivity{

    private ActionBar actionBar;
    private String newVersion;
    private MotionButton line,blood,doctor ,professions,satota,edit,share;
    private FloatingActionButton floatingActionButton;
    private FirebaseRemoteConfig remoteConfig;
    private int currentVersionCod;
    private FirebaseFirestore db;
    private String [] types;
    private FirebaseAuth mAuth;
    public ProgressBar progressBar;
    private EditAll editAll;
    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_select);
        getObj();
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("ar");
        line.setOnClickListener(View -> {
                startActivity(new Intent(
                    SelectActivity.this,
                    Transportation_linesActivity.class));
        });
        blood.setOnClickListener( View-> {

                startActivity(new Intent(SelectActivity.this,TypeActivity.class));

        });
        doctor.setOnClickListener(View -> {

                startActivity(new
                    Intent(
                        SelectActivity.this,DoctorActivity.class));

        });
        floatingActionButton.setOnClickListener(View -> {
                 showDialog();
        });
        professions.setOnClickListener(view -> {
            startActivity(new
                Intent(
                SelectActivity.this,ProfessionUserActivity.class));
        });
        satota.setOnClickListener(View -> {
             startActivity(new
           Intent(
            SelectActivity.this, SatotUsertActivity.class));
           });
// ! Here to update data in all applications;

        edit.setOnClickListener(View ->{
            showDialogUpdate();
         });

        share.setOnClickListener(View -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=com.Blood.types");
            sendIntent.putExtra(Intent.EXTRA_TITLE,
                    "This is a link to App in Google Play");
            startActivity(sendIntent);

        });

       // below code to update app in on create
        currentVersionCod = getCurrentVersionCode();
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings
            configSettings = new
            FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(5000)
            .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>()
        {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {

                //newVersion get number integer from firebase
                if (task.isSuccessful()){
                    newVersion = remoteConfig.getString("update");
                    if (Integer.parseInt(newVersion) > currentVersionCod) {
                        showUpdateDialog();
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


    private void showDialogUpdate() {
        types = new String[]{
          getString(R.string.blood_type),
            getString(R.string.doctor),
            getString ( R.string.professions),
            getString (R.string.internal_transfer),
            getString (R.string.transmission_lines)
        };
        editAll = new EditAll(this);
        ArrayAdapter<String> arrayAdapter =
            new ArrayAdapter<>(this,R.layout.drop_down_item,types);
        View view = LayoutInflater.from(SelectActivity.this).inflate(
            R.layout.update_all,
            null,
            false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        com.google.android.material.textfield.TextInputEditText
            edit = view.findViewById(R.id.number);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MotionButton search = view.findViewById(R.id.searchNumber);
        MotionButton request = view.findViewById(R.id.requestDelete);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typesAuto);
        autoCompleteTextView.setAdapter(arrayAdapter);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        search.setOnClickListener(View->{
            String number = edit.getText().toString();
            String service = autoCompleteTextView.getText().toString();
            if (number.isEmpty() || service.isEmpty()){
                Toast.makeText(this, "isEmpty", Toast.LENGTH_SHORT).show();
            }else if (number.length() < 11){
                Toast.makeText(this, R.string.short_number, Toast.LENGTH_SHORT).show();

            }else {
                    editAll.getData(service,number);
                    progressBar.setVisibility(android.view.View.VISIBLE);
                    dialog.dismiss();
            }

        });
        request.setOnClickListener(View ->{

        });
    }

    private void getObj() {
        actionBar = getSupportActionBar();
        actionBar.hide();
        db = FirebaseFirestore.getInstance();

        professions = findViewById(R.id.professions);
        line = findViewById(R.id.lineTravel);
        blood = findViewById(R.id.bloods);
        doctor = findViewById(R.id.doctor);
        satota = findViewById(R.id.satota);
        floatingActionButton = findViewById(R.id.Add);
        edit = findViewById(R.id.edit);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        share = findViewById(R.id.share);
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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MotionButton addLine = v.findViewById(R.id.line_request);
        MotionButton addDoctor = v.findViewById(R.id.doctorAdd);
        MotionButton addBlood = v.findViewById(R.id.add_blood);
        MotionButton addSatota = v.findViewById(R.id.add_toktok);

        MotionButton add_Profession = v.findViewById(R.id.add_Profession);
        builder.setView(v);
        AlertDialog dialog = builder.create();


        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this, LineActivity.class));
                dialog.dismiss();
            }
        });
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(SelectActivity.this, Sendrequest.class));
                dialog.dismiss();
            }
        });
        addBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelectActivity.this, RegisterActivity.class);
//                intent.putExtra("isEditMode", true);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        add_Profession.setOnClickListener(View ->{
            startActivity(new Intent(SelectActivity.this, ProfessionActivity.class));
            dialog.dismiss();
        });
        addSatota.setOnClickListener(View ->{

            Intent intent = new Intent(SelectActivity.this, SatotaRegisterActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();

    }


}