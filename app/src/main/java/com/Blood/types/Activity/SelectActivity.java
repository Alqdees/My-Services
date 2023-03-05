package com.Blood.types.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.Blood.types.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SelectActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private MotionButton line,blood,doctor;
//    private MeowBottomNavigation navigation;
    private FloatingActionButton floatingActionButton;

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
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_main,null,false);
        MotionButton addLine = v.findViewById(R.id.line_request);
        MotionButton addDoctor = v.findViewById(R.id.doctorAdd);
        MotionButton addBlood = v.findViewById(R.id.add_blood);
        builder.setView(v);

        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this,LineActivity.class));
            }
        });
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(SelectActivity.this,Sendrequest.class));
            }
        });
        addBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectActivity.this,RegisterActivity.class));
            }
        });

        builder.create().show();

    }
}