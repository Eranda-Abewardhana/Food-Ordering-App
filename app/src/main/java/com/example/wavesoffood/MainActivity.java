package com.example.wavesoffood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wavesoffood.Adapters.VPAdapter;
import com.example.wavesoffood.Fragments.CartFragment;
import com.example.wavesoffood.Fragments.CheckoutFragment;
import com.example.wavesoffood.Fragments.HomeFragment;
import com.example.wavesoffood.Fragments.LogoutFragment;
import com.example.wavesoffood.loginregistration.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static FirebaseAuth auth;
    FirebaseUser user;
    public static TabLayout tableLayout;
    public static ViewPager2 viewPager;
    public static ImageView cancel,addCart;
    public static VPAdapter vpAdapter;
    LogoutFragment logoutFragment;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        cancel = findViewById(R.id.fab);
        addCart = findViewById(R.id.fab2);
        tableLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.pager);

        vpAdapter = new VPAdapter(this);
        vpAdapter.addFragment(new HomeFragment(), "Home", R.drawable.home);
        vpAdapter.addFragment(new CartFragment(), "Cart", R.drawable.cart_white);
        vpAdapter.addFragment(new CheckoutFragment(), "Checkout", R.drawable.checkout);

        logoutFragment = new LogoutFragment();
        vpAdapter.addFragment(new LogoutFragment(), "Logout", R.drawable.baseline_logout_24);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewPager.setAdapter(vpAdapter);

        new TabLayoutMediator(tableLayout, viewPager,
                (tab, position) -> {
                    tab.setIcon(vpAdapter.getIcon(position));
                }).attach();

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1 && GlobalVariables.cartList.size()>0 && CartFragment.empty != null) {
                } else {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}