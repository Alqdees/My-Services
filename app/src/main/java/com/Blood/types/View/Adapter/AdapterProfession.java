package com.Blood.types.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    return new ProfessionAD(v) ;
  }

  @Override
  public void onBindViewHolder(@NonNull ProfessionAD holder, int position) {

  }

  @Override
  public int getItemCount() {
    if (professions != null)
      return professions.size();
    else
      return 0;

  }

  public class ProfessionAD extends RecyclerView.ViewHolder{

    public ProfessionAD(@NonNull View itemView) {
      super(itemView);
    }
  }
}
