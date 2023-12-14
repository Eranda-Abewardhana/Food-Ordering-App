package com.example.wavesoffood.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> fragmentTitle = new ArrayList<>();
    private ArrayList<Integer> fragmentIcon = new ArrayList<>(); // Add this line

    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title, int icon) {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
        fragmentIcon.add(icon); // Add this line
    }

//    public CharSequence getPageTitle(int position) {
//        return fragmentTitle.get(position);
//    }
    public int getIcon(int position) {
        return fragmentIcon.get(position);
}
}
