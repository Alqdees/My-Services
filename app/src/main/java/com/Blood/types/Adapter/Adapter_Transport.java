package com.Blood.types.Adapter;

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

import com.Blood.types.Model.ModelTransport;
import com.Blood.types.R;

import java.util.ArrayList;

public class Adapter_Transport extends RecyclerView.Adapter<Adapter_Transport.AdapterTransport> {

    private ArrayList<ModelTransport> modelTransports;
    private Context context;



    public Adapter_Transport(Context context, ArrayList<ModelTransport> list){
        this.context = context;
        this.modelTransports= list;

    }
    @NonNull
    @Override
    public AdapterTransport onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(context).inflate(R.layout.cardview_transport,parent,false);
        return new AdapterTransport(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTransport holder, int position) {
        ModelTransport models = modelTransports.get(position);
        String name = models.getName();
        String number= models.getNumber();
        String type = models.getType();
        String time = models.getTime();
        String from = models.getFrom();

        holder.tv_name.setText(name);
        holder.tv_number.setText(number);
        holder.tv_type.setText(type);
        holder.tv_time.setText(time);
        holder.tv_from.setText(from);
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
       return modelTransports.size();
    }

    class AdapterTransport extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_number,tv_type,tv_time,tv_from;
        private ImageButton callBtn;
        public AdapterTransport(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.name);
            tv_number=itemView.findViewById(R.id.Number);
            tv_type= itemView.findViewById(R.id.type);
            tv_time=itemView.findViewById(R.id.time);
            tv_from=itemView.findViewById(R.id.fromanto);
            callBtn = itemView.findViewById(R.id.call);
        }
    }
}
