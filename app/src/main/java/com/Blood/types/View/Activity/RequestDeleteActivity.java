package com.Blood.types.View.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.content.Intent;
import android.net.Uri;
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
    Intent intent = new Intent(Intent.ACTION_VIEW);

    // Set the data URI to the WhatsApp URI scheme with the phone number
    // Replace the country code prefix (+xx) with the appropriate country code
    // For example, for the US, it would be "1" as in "+11234567890"
    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+9647824854526" );
    intent.setData(uri);

    // Add flags to the Intent to make sure it opens the WhatsApp app
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    // Start the Intent
    startActivity(intent);
});
tv_messenger.setOnClickListener((View) ->{
    Intent intent = new Intent(Intent.ACTION_VIEW);

    // Set the data URI to the Telegram URI scheme with your username
    intent.setData(Uri.parse("https://m.me/AH95ED"));

    // Add flags to the Intent to make sure it opens the Telegram app
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    // Start the Intent
    startActivity(intent);
});

tv_telegram.setOnClickListener((View) ->{
    Intent intent = new Intent(Intent.ACTION_VIEW);

    // Set the data URI to the Telegram URI scheme with your username
    intent.setData(Uri.parse("https://t.me/Ah9_5D"));

    // Add flags to the Intent to make sure it opens the Telegram app
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    // Start the Intent
    startActivity(intent);
});


    }

    private void sendInstagram() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        // Set the data URI to the Instagram profile URI scheme with the username
        Uri uri = Uri.parse("http://instagram.com/_u/ah_0.sh" );
        intent.setData(uri);

        // Add flags to the Intent to make sure it opens the Instagram app
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Start the Intent
        startActivity(intent);
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