package com.example.inganapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.inganapp.fragments.HomeFragment;
import com.example.inganapp.fragments.NotificationFragment;
import com.example.inganapp.fragments.SettingsFragment;

public class MyViewPagerAdapter  extends FragmentStateAdapter {
    private String result, picture;
    //private Uri uri;
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String result, String picture) {
        super(fragmentActivity);
        this.result = result;
        this.picture = picture;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        bundle.putString("picture", picture);


        switch (position){
            case 0:
                fragment=new HomeFragment();
                fragment.setArguments(bundle);

                break;
            case 1:
                fragment= new NotificationFragment();
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment= new SettingsFragment();
                fragment.setArguments(bundle);
                break;
            default:
                fragment= new HomeFragment();
                fragment.setArguments(bundle);
                break;
        }
        return  fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
