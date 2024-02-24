package com.example.wavesoffood.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wavesoffood.Fragments.LogoutFragment;

import java.util.ArrayList;

public class VPAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> fragmentTitle = new ArrayList<>();
    private ArrayList<Integer> fragmentIcon = new ArrayList<>();
    LogoutFragment logoutFragment;

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
        fragmentIcon.add(icon);

        // If the added fragment is a LogoutFragment, keep a reference
        if (fragment instanceof LogoutFragment) {
            logoutFragment = (LogoutFragment) fragment;
        }
    }

    public int getIcon(int position) {
        return fragmentIcon.get(position);
    }

}
