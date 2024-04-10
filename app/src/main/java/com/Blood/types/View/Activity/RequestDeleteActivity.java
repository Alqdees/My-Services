package com.Blood.types.View.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.utils.widget.MotionButton;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.Blood.types.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class RequestDeleteActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private com.google.android.material.textfield.TextInputEditText
            Et_name,Et_number,Et_des;
    private ImageView tv_whatsapp,tv_telegram,tv_instagram,tv_messenger;
    private MotionButton btn_request;
   private DatabaseReference databaseReference;
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
           sendSocial("http://instagram.com/_u/ah_0.sh");
        });

tv_whatsapp.setOnClickListener((View) ->{
    sendSocial("https://api.whatsapp.com/send?phone=+9647824854526" );
});
tv_messenger.setOnClickListener((View) ->{
    sendSocial("https://m.me/AH95ED");

});

tv_telegram.setOnClickListener((View) ->{

    sendSocial("https://t.me/Ah9_5D");

});
btn_request.setOnClickListener((View) ->{

sendData();
});


    }

    private void sendData() {
        String number = Et_number.getText().toString();
        String name = Et_name.getText().toString();
        String description = Et_des.getText().toString();
        if (name.isEmpty() || description.isEmpty() || number.isEmpty()){
            Toast.makeText(this, "Check All Filed", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();

        // Set values in the HashMap
        hashMap.put("name", name);
        hashMap.put("number",number );
        hashMap.put("description",description );
        databaseReference.child("Edit").child(number).setValue(hashMap).addOnCompleteListener((T)->{
            Toast.makeText(this, ""+T.isSuccessful(), Toast.LENGTH_SHORT).show();
        }).addOnFailureListener((F) -> {
            Toast.makeText(this, ""+F.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }


    private void getObj() {

        // initialize
        databaseReference = FirebaseDatabase.getInstance().getReference();
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

   private void sendSocial(String url) {
       Intent intent = new Intent(Intent.ACTION_VIEW);

       // Set the data URI to the Instagram profile URI scheme with the username
       Uri uri = Uri.parse(url );
       intent.setData(uri);

       // Add flags to the Intent to make sure it opens the Instagram app
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

       // Start the Intent
       startActivity(intent);
    }
}