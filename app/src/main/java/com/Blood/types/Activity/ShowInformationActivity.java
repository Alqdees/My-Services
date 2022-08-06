package com.Blood.types.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.Blood.types.R;
import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.Field;

public class ShowInformationActivity extends AppCompatActivity {

    private MaterialTextView tv_name,tv_number,tv_type,tv_location;
    private String name,number,type,location;
    private MotionButton Call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_information);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        try {
            @SuppressLint("DiscouragedPrivateApi")
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }


        tv_name = findViewById(R.id.name);
        tv_number = findViewById(R.id.number);
        tv_type = findViewById(R.id.type);
        tv_location = findViewById(R.id.location);
        Call = findViewById(R.id.call);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        type = intent.getStringExtra("type");
        location = intent.getStringExtra("location");
        tv_name.setText(name);
        tv_number.setText(number);
        tv_type.setText(type);
        tv_location.setText(location);

        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_DIAL);
                share.setData(Uri.parse("tel:"+number));
                startActivity(share);

            }
        });
    }
}