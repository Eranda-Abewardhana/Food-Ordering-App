package com.example.wavesoffood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wavesoffood.Database.DishDTO;
import com.example.wavesoffood.R;
import com.example.wavesoffood.models.JsonModel;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHloder> {

    Context context;
    List<DishDTO> jsonModels;
    public ItemAdapter(Context context, List<DishDTO> jsonModels) {
        this.context = context;
        this.jsonModels = jsonModels;
    }
    public static class ItemViewHloder extends RecyclerView.ViewHolder{

        TextView name;
        TextView price;
        public ItemViewHloder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.amount);
        }


    }
    @NonNull
    @Override
    public ItemAdapter.ItemViewHloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ItemViewHloder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHloder holder, int position) {
        DishDTO p = jsonModels.get(position);
        holder.name.setText(p.getName());
        holder.price.setText(String.valueOf(p.getPrice()));
    }

    @Override
    public int getItemCount() {
        return jsonModels.size();
    }
}
