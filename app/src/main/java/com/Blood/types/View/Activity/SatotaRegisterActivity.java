package com.Blood.types.View.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.android.material.textfield.TextInputEditText;

public class SatotaRegisterActivity extends AppCompatActivity {

  private TextInputEditText E_name,E_number,E_locationWork;
  private MotionButton addSatota;
  private ProgressBar progressBar;
  private String name,number,location;
  private ActionBar actionBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    actionBar = getSupportActionBar();
    actionBar.hide();
    setContentView(R.layout.activity_satota);

    getAllObject();

    addSatota.setOnClickListener(View ->{

      name = E_name.getText().toString();
      number = E_number.getText().toString();
      location = E_locationWork.getText().toString();

      if (name.isEmpty() || number.isEmpty() || location.isEmpty()){
        Toast.makeText(this, "isEmpty", Toast.LENGTH_SHORT).show();
      }else {
        // do some thing ...
        Toast.makeText(this, "Do Some Thing ...", Toast.LENGTH_SHORT).show();

      }
    });

  }

  private void getAllObject() {

    E_name = findViewById(R.id.Sname);
    E_number = findViewById(R.id.Snumber);
    E_locationWork = findViewById(R.id.Slocation);
    addSatota = findViewById(R.id.addSatota);
    progressBar =findViewById(R.id.Sprogress);
    progressBar.setVisibility(View.INVISIBLE);
  }
}