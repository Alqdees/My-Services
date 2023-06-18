package com.Blood.types.Controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.Blood.types.R;
import com.Blood.types.View.Activity.SelectActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;


public class EditAll {

  private String [] bloodTypes;

  private Context ctx;
  private FirebaseFirestore db;

  public EditAll(Context context){
    this.ctx = context;
    db = FirebaseFirestore.getInstance();

  }
  private void getData(String service, String num) {
    if (service.equals(ctx.getString(R.string.blood_type))) {
      bloodTypes = new String[]{
          "A+",
          "B+",
          "A-",
          "B-",
          "O+",
          "O-",
          "AB+",
          "AB-",
          "لا أعرف"
      };
      CollectionReference collectionRef = db.collection(service);

      collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
              if (Objects.equals(document.getString("number"), num)) {
                if (num.charAt(0) == '0') {
                  String realNumber = num.substring(1);
//                sendSMSCode(realNumber,service);
                  Log.d("GET_NUMBER", realNumber);
                  break;
                }
              }
            }
          } else {
            Toast.makeText(
                ctx,
                "Some Error",
                Toast.LENGTH_SHORT).show();
            ((SelectActivity) ctx).progressBar.setVisibility(View.INVISIBLE);

//          progressBar.setVisibility(View.INVISIBLE);
          }

        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(
              ctx,
              e.getMessage(),
              Toast.LENGTH_SHORT).show();
//        progressBar.setVisibility(View.INVISIBLE);
        }
      });
    }


  }
  public void searchNumber(String number, String service) {

    if (service.equals(ctx.getString(R.string.blood_type))){
      bloodTypes = new String[]{
          "A+",
          "B+",
          "A-",
          "B-",
          "O+",
          "O-",
          "AB+",
          "AB-",
          "لا أعرف"
      };

      for (String s:bloodTypes) {

        CollectionReference collectionRef = db.collection(s);
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (QueryDocumentSnapshot document : task.getResult()) {
                if (Objects.equals(document.getString("number"), number)) {
                  if (number.charAt(0) == '0'){
                    String realNumber = number.substring(1);
                    Toast.makeText(ctx, realNumber, Toast.LENGTH_SHORT)
                        .show();
                    System.out.println(realNumber);
                    ((SelectActivity) ctx).progressBar.setVisibility(View.INVISIBLE);
                        break;
                  }
                }
              }
            }else {
              Toast.makeText(
                  ctx,task.getException().getMessage(),
                  Toast.LENGTH_SHORT).show();
              ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);
            }

//            return false;
          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(
                ctx,
                e.getMessage(),
                Toast.LENGTH_SHORT).show();
            ((SelectActivity)ctx).progressBar.setVisibility(View.INVISIBLE);
          }

        });
      }
    }
    else if (service.equals(ctx.getString(R.string.doctor))) {
      getData("Doctor", number);
    } else if (service.equals(ctx.getString(R.string.professions))) {
      getData("professions",number);
    }else if (service.equals(ctx.getString(R.string.internal_transfer))){
      getData("Satota",number);
    }else if (service.equals(ctx.getString(R.string.transmission_lines))){
      getData("line",number);
    }
  }



}
