package com.Blood.types.View.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.os.Bundle;
import android.widget.ImageView;

import com.Blood.types.R;

public class RequestDeleteActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private com.google.android.material.textfield.TextInputEditText
            Et_name,Et_number,Et_des;
    private ImageView tv_whatsapp,tv_telegram,tv_instagram,tv_messenger;
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

        tv_instagram.setOnClickListener((View) ->{
            sendInstagram();
        });

tv_whatsapp.setOnClickListener((View) ->{

});
tv_messenger.setOnClickListener((View) ->{

});

tv_telegram.setOnClickListener((View) ->{

});


    }

    private void sendInstagram() {

    }

    private void getObj() {

        // initialize

        Et_name = findViewById(R.id.nameEt);
        Et_number = findViewById(R.id.numberEt);
        Et_des = findViewById(R.id.desEt);
        // initialize social
        btn_request = findViewById(R.id.registerBtn);
        tv_instagram = findViewById(R.id.instagram);
        tv_telegram = findViewById(R.id.telegram);
        tv_whatsapp = findViewById(R.id.whats_app);
        tv_messenger = findViewById(R.id.messenger);
        //


    }
}