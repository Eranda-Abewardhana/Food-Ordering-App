package com.example.wavesoffood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wavesoffood.Adapters.VPAdapter;
import com.example.wavesoffood.Fragments.CartFragment;
import com.example.wavesoffood.Fragments.CheckoutFragment;
import com.example.wavesoffood.Fragments.HomeFragment;
import com.example.wavesoffood.loginregistration.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    public static TabLayout tableLayout;
    public static ViewPager2 viewPager;
    public static ImageView cancel,addCart;
    public static VPAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DAO.init(getApplicationContext());
        //initialize
        auth = FirebaseAuth.getInstance();
//        button = findViewById(R.id.logout);
//        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if(user == null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
//        else
//        {
//            textView.setText(user.getEmail());
//        }

        cancel = findViewById(R.id.fab);
        addCart = findViewById(R.id.fab2);
        tableLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.pager);
//        button = findViewById(R.id.logout);  // Assuming you have a button with the id 'logout'

                  vpAdapter = new VPAdapter(this); // 'this' refers to your FragmentActivity
        vpAdapter.addFragment(new HomeFragment(), "Home", R.drawable.home); // Replace ic_home with your actual drawable resource
        vpAdapter.addFragment(new CartFragment(), "Cart", R.drawable.cart_white); // Replace ic_cart with your actual drawable resource
        vpAdapter.addFragment(new CheckoutFragment(), "Checkout", R.drawable.checkout); // Replace ic_checkout with your actual drawable resource

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                GlobalVariables.cartList.clear();
//                HomeFragment.itemAdapter.notifyDataSetChanged();
            }
        });

        viewPager.setAdapter(vpAdapter); // Set the adapter first

        new TabLayoutMediator(tableLayout, viewPager,
                (tab, position) -> {
                    tab.setIcon(vpAdapter.getIcon(position)); // Set the icon for the tab
                }).attach();


// ...

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }
}