package com.Blood.types.View.Adapter;

import android.annotation.SuppressLint;
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

import com.Blood.types.Model.Satota;
import com.Blood.types.R;

import java.util.ArrayList;

public class SatotaAdapter extends RecyclerView.Adapter<SatotaAdapter.AdapterSatota>{
  private Context context;
  private ArrayList<Satota> satotas;

  @SuppressLint("NotifyDataSetChanged")
  public SatotaAdapter(Context context, ArrayList<Satota> satotas){
    this.context = context;
    this.satotas = satotas;
    notifyDataSetChanged();
  }
  @NonNull
  @Override
  public AdapterSatota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.satota_cardview,parent,false);
    return new AdapterSatota(v);
  }

  @Override
  public void onBindViewHolder(@NonNull AdapterSatota holder, int position) {

    Satota satota = satotas.get(position);
    String name = satota.getName();
    String number= satota.getNumber();
    String location = satota.getLocation();

    holder.tv_name.setText(name);
    holder.tv_number.setText(number);
    holder.tv_location.setText(location);
    holder.imageButton.setOnClickListener(View ->{

      Intent share = new Intent(Intent.ACTION_DIAL);
      share.setData(Uri.parse("tel:"+number));
      context.startActivity(share);
    });
    holder.getAdapterPosition();
  }
  @Override
  public int getItemCount() {
    return satotas.size();
  }

  class AdapterSatota extends RecyclerView.ViewHolder{
    private TextView tv_name,tv_number,tv_location;
    private ImageButton imageButton;
    public AdapterSatota(@NonNull View v) {
      super(v);
      tv_name = v.findViewById(R.id.name);
      tv_number = v.findViewById(R.id.Number);
      tv_location = v.findViewById(R.id.location);
      imageButton = v.findViewById(R.id.call);
    }
  }
}
