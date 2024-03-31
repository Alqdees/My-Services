package com.Blood.types.View.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.os.Bundle;
import com.Blood.types.R;

public class RequestDeleteActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private com.google.android.material.textfield.TextInputEditText
            Et_name,Et_number,Et_des,Et_location;
    private MotionButton btn_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_request_delete);
        actionBar = getSupportActionBar();
        actionBar.hide();
        getObj();
        //!///////////////




    }

    private void getObj() {
        Et_name = findViewById(R.id.nameEt);
        Et_number = findViewById(R.id.numberEt);
        Et_des= findViewById(R.id.desEt);
        Et_location = findViewById(R.id.locationEt);
        btn_request = findViewById(R.id.registerBtn);

    }
}