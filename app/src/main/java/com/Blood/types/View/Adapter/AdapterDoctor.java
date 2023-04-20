package com.Blood.types.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Blood.types.Model.Doctor;
import com.Blood.types.R;

import java.util.ArrayList;

public class AdapterDoctor extends RecyclerView.Adapter<AdapterDoctor.Doctor_Adapter> {
    private Context context;
    private ArrayList<Doctor> doctors;
    public AdapterDoctor(Context context, ArrayList<Doctor> doctors)
        {
        this.context= context;
        this.doctors=doctors;
    }
    @NonNull
    @Override
    public Doctor_Adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =
                LayoutInflater.from(context).inflate(R.layout.cardview_doctor,
                        parent,false);
        return new Doctor_Adapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Doctor_Adapter holder, int position) {
        Doctor doctor = doctors.get(position);
        String name = doctor.getName();
        String specialization = doctor.getSpecialization();
        String presence = doctor.getPresence();
        String number = doctor.getNumber();
        String title = doctor.getTitle();
        holder.name.setText(name);
        holder.specialization.setText(specialization);
        holder.presence.setText(presence);
        holder.number.setText(number);
        holder.title.setText(title);
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_DIAL);
                share.setData(Uri.parse("tel:"+number));
                context.startActivity(share);
            }
        });
        holder.getAdapterPosition();


    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }
    public class Doctor_Adapter extends RecyclerView.ViewHolder{

        private TextView name,number,title,specialization,presence;
        private ImageButton callBtn;


        public Doctor_Adapter(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            specialization= itemView.findViewById(R.id.specialization);
            presence = itemView.findViewById(R.id.presence);
            number = itemView.findViewById(R.id.Number);
            title = itemView.findViewById(R.id.title);
            callBtn = itemView.findViewById(R.id.call);

        }
    }
}
