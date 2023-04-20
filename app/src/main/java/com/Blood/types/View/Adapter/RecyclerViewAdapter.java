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

import com.Blood.types.Model.Model;
import com.Blood.types.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AdapterBelow> {

    private Context context;
    private ArrayList<Model> models;
    //Constructor
    public RecyclerViewAdapter(Context context,ArrayList<Model> models){

        this.context = context;
        this.models = models;

    }
    @NonNull
    @Override
    public AdapterBelow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);

        return new AdapterBelow(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBelow holder, int position) {
        Model model = models.get(position);

        String name = model.getName();
        String number = model.getNumber();
        String type = model.getType();
        String location = model.getLocation();


        holder.name.setText(name);
        holder.number.setText(number);
        holder.type.setText(type);
        holder.location.setText(location);
        holder.getAdapterPosition();
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_DIAL);
                share.setData(Uri.parse("tel:"+number));
                context.startActivity(share);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class AdapterBelow extends RecyclerView.ViewHolder{
         TextView name,number,type,location;
         private ImageButton call;
        public AdapterBelow(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.Number);
            type = itemView.findViewById(R.id.type);
            location = itemView.findViewById(R.id.location);
            call = itemView.findViewById(R.id.call);

        }
    }
}
