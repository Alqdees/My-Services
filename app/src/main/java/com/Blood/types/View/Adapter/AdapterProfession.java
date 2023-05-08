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

import com.Blood.types.Model.Profession;
import com.Blood.types.R;

import java.util.ArrayList;

public class AdapterProfession extends RecyclerView.Adapter<AdapterProfession.ProfessionAD> {

  private ArrayList<Profession> professions;
  private Context context;

  public AdapterProfession(ArrayList<Profession> professions, Context context) {
    this.professions = professions;
    this.context = context;
  }

  @NonNull
  @Override
  public ProfessionAD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(context).inflate(R.layout.profession_adapter,parent,false);

    return new ProfessionAD(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ProfessionAD holder, int position) {
    Profession profession = professions.get(position);

    holder.tv_name.setText(profession.getName());
    holder.tv_professions.setText(profession.getNameProfession());
    holder.tv_number.setText(profession.getNumber());

    holder.buttonCall.setOnClickListener(view -> {

      Intent share = new Intent(Intent.ACTION_DIAL);
      share.setData(Uri.parse("tel:"+profession.getNumber()));
      context.startActivity(share);


    });
    holder.getAdapterPosition();



  }

  @Override
  public int getItemCount() {
    if (professions != null)
      return professions.size();
    else
      return 0;

  }

  public static class ProfessionAD extends RecyclerView.ViewHolder{
    private TextView tv_name,tv_professions,tv_number;
    private ImageButton buttonCall;

    public ProfessionAD(@NonNull View itemView) {
      super(itemView);
      tv_name = itemView.findViewById(R.id.name);
      tv_professions = itemView.findViewById(R.id.professions);
      tv_number = itemView.findViewById(R.id.Number);
      buttonCall = itemView.findViewById(R.id.call);


    }
  }
}
