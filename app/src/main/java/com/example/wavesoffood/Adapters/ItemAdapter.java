package com.example.wavesoffood.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wavesoffood.Database.DishDTO;
import com.example.wavesoffood.Fragments.CartFragment;
import com.example.wavesoffood.Fragments.HomeFragment;
import com.example.wavesoffood.GlobalVariables;
import com.example.wavesoffood.R;
import com.example.wavesoffood.models.JsonModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHloder> {

    Context context;
    List<DishDTO> jsonModels;
    int count;
    DecimalFormat decformat = new DecimalFormat("#,##0.00");

    public ItemAdapter(Context context, List<DishDTO> jsonModels) {
        this.context = context;
        this.jsonModels = jsonModels;
    }
    public static class ItemViewHloder extends RecyclerView.ViewHolder{

        TextView name,count;
        TextView price,total;
        ImageView plus;
        ImageView minus;
        public ItemViewHloder(@NonNull View itemView) {
            super(itemView);
            name  = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.amount);
            count = itemView.findViewById(R.id.count);
            plus  = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            count = itemView.findViewById(R.id.count);
            total = itemView.findViewById(R.id.total);
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
        holder.price.setText(decformat.format(p.getPrice()));
        holder.count.setText(String.valueOf(p.getQty()));
        holder.total.setText(decformat.format(p.getPrice()*p.getQty()));
        if(p.getQty() > 0 ){
            holder.total.setVisibility(View.VISIBLE);
        }
        else {
            holder.total.setVisibility(View.GONE);
        }
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = p.getQty();
                count += 1;
                p.setQty(count);
                notifyDataSetChanged();
                HomeFragment.itemAdapter.notifyDataSetChanged();
                if(CartFragment.itemAdapter2 != null)
                    CartFragment.itemAdapter2.notifyDataSetChanged();
                cartUpdate(p);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = p.getQty() -1 ;
                if(count > -1)
                    p.setQty(count);
                    notifyDataSetChanged();
                    HomeFragment.itemAdapter.notifyDataSetChanged();
                    if(CartFragment.itemAdapter2 != null)
                        CartFragment.itemAdapter2.notifyDataSetChanged();
                    cartUpdate(p);
            }
        });
        //                count = p.getQty();

    }

    private void cartUpdate(DishDTO p) {
        if( count > 0 && GlobalVariables.cartList.size() > 0 ){
//            for(DishDTO item : jsonModels){
            boolean check = false;
                for (DishDTO addedItem : GlobalVariables.cartList){
                    if(p.getId() == addedItem.getId()){
                        check = true;
                        addedItem.setQty(p.getQty());
                        break;
                    }
                }
                if(!check){
                    GlobalVariables.cartList.add(p);
                }
//            }
        }
        else if (count > 0){
            GlobalVariables.cartList.add(p);
        }
        else if( count == 0 ){
//            for(DishDTO item : jsonModels){
                for (DishDTO addedItem : GlobalVariables.cartList){
                    if(p.getId() == addedItem.getId()){
                        GlobalVariables.cartList.remove(addedItem);
                        break;
                    }
//                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return jsonModels.size();
    }
}
