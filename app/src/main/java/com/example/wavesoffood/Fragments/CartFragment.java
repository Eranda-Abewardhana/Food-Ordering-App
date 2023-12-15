package com.example.wavesoffood.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wavesoffood.Adapters.ItemAdapter;
import com.example.wavesoffood.Database.DishDTO;
import com.example.wavesoffood.GlobalVariables;
import com.example.wavesoffood.R;

import java.text.DecimalFormat;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CardView cardView;
    private TextView test,net,qty;
    public ImageView cancel,ckeckout,payment;
    public static ItemAdapter itemAdapter2;
    DecimalFormat decformat = new DecimalFormat("#,##0.00");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view    = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recycle);
        cardView     = view.findViewById(R.id.card2);
        test         = view.findViewById(R.id.textView5);
        net          = view.findViewById(R.id.net);
        qty          = view.findViewById(R.id.qty);
        cancel       = view.findViewById(R.id.fab);

        GlobalVariables.total = 0;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        itemAdapter2 = new ItemAdapter(getContext(), GlobalVariables.cartList);
        recyclerView.setAdapter(itemAdapter2);
        itemAdapter2.notifyDataSetChanged();

//        cancel.setVisibility(View.GONE);

        if(GlobalVariables.cartList.size() > 0 ){
            recyclerView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
//            test.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
//            test.setVisibility(View.VISIBLE);
        }
        for (int i = 0 ; i < GlobalVariables.cartList.size();i++){
            DishDTO item   = GlobalVariables.cartList.get(i);
            GlobalVariables.total += item.getPrice() * item.getQty();
        }
        net.setText(decformat.format(GlobalVariables.total));
        qty.setText(String.valueOf(GlobalVariables.cartList.size()));
        return view;
    }
}