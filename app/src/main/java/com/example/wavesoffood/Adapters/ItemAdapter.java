package com.example.wavesoffood.Adapters;

import static com.example.wavesoffood.MainActivity.viewPager;
import static com.example.wavesoffood.MainActivity.vpAdapter;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.wavesoffood.Database.DishDTO;
import com.example.wavesoffood.Fragments.CartFragment;
import com.example.wavesoffood.Fragments.HomeFragment;
import com.example.wavesoffood.GlobalVariables;
import com.example.wavesoffood.MainActivity;
import com.example.wavesoffood.R;
import com.example.wavesoffood.models.JsonModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
        ImageView minus,del;
        public ItemViewHloder(@NonNull View itemView) {
            super(itemView);
            name  = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.amount);
            count = itemView.findViewById(R.id.count);
            plus  = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            count = itemView.findViewById(R.id.count);
            total = itemView.findViewById(R.id.total);
            del   = itemView.findViewById(R.id.del);
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
        MainActivity.tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Tab selected, you can get the current tab here
                int currentTabPosition = tab.getPosition();
                if(currentTabPosition == 0){
                    MainActivity.addCart.setVisibility(View.VISIBLE);
                    MainActivity.cancel.setVisibility(View.VISIBLE);
                }
                else
                {
                    MainActivity.addCart.setVisibility(View.GONE);
                    MainActivity.cancel.setVisibility(View.GONE);
                }
                if(currentTabPosition == 1 && CartFragment.empty  != null){
                    holder.del.setVisibility(View.VISIBLE);

                    if(GlobalVariables.cartList.size() > 0){
                        CartFragment.empty.setVisibility(View.GONE);
                        CartFragment.recyclerView.setVisibility(View.VISIBLE);
                    }else {
                        CartFragment.empty.setVisibility(View.VISIBLE);
                        CartFragment.recyclerView.setVisibility(View.GONE);
                    }
                }
                else {
                    holder.del.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(p.getQty() > 0 ){
            holder.total.setVisibility(View.VISIBLE);
        }
        else {
            holder.total.setVisibility(View.GONE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p.getQty() > 0){
                    for (DishDTO addedItem : GlobalVariables.cartList){
                        if(p.getId() == addedItem.getId()){
                            GlobalVariables.cartList.remove(addedItem);
                            p.setQty(0);
                            break;
                        }
                    }
                }
                CartFragment.totalUpdate();
                notifyDataSetChanged();
                HomeFragment.itemAdapter.notifyDataSetChanged();
                if(CartFragment.itemAdapter2 != null)
                    CartFragment.itemAdapter2.notifyDataSetChanged();
                enableIcon();
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = p.getQty();
                count += 1;
                p.setQty(count);
                cartUpdate(p);
                CartFragment.totalUpdate();
                notifyDataSetChanged();
                HomeFragment.itemAdapter.notifyDataSetChanged();
                if(CartFragment.itemAdapter2 != null)
                    CartFragment.itemAdapter2.notifyDataSetChanged();
                enableIcon();
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = p.getQty() -1 ;
                if(count > -1)
                    p.setQty(count);
                    cartUpdate(p);
                    CartFragment.totalUpdate();
                    notifyDataSetChanged();
                    HomeFragment.itemAdapter.notifyDataSetChanged();
                    if(CartFragment.itemAdapter2 != null)
                        CartFragment.itemAdapter2.notifyDataSetChanged();
                    enableIcon();
            }
        });
    }

    private void enableIcon() {
        if(GlobalVariables.cartList.size() > 0){
            MainActivity.cancel.setVisibility(View.VISIBLE);
            MainActivity.addCart.setVisibility(View.VISIBLE);
        }else {
            MainActivity.cancel.setVisibility(View.GONE);
            MainActivity.addCart.setVisibility(View.GONE);
        }
    }

    private void cartUpdate(DishDTO p) {
        if( count > 0 && GlobalVariables.cartList.size() > 0 ){
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
        }
        else if (count > 0){
            GlobalVariables.cartList.add(p);
        }
        else if( count == 0 ){
                for (DishDTO addedItem : GlobalVariables.cartList){
                    if(p.getId() == addedItem.getId()){
                        GlobalVariables.cartList.remove(addedItem);
                        break;
                    }
            }
        }
    }

    @Override
    public int getItemCount() {
        return jsonModels.size();
    }
}
