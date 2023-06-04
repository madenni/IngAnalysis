package com.example.inganapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.inganapp.fragments.HomeFragment;
import com.example.inganapp.fragments.NotificationFragment;
import com.example.inganapp.fragments.SettingsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] { R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3 };
    private final Context mContext;
    private String result, picture;
    public MyViewPagerAdapter(Context context, FragmentManager fm, String result, String picture) {
        super(fm);
        mContext = context;
        this.result = result;
        this.picture = picture;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        bundle.putString("picture", picture);
        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                fragment = HomeFragment.newInstance();
                fragment.setArguments(bundle);

                break;
            case 1:
                fragment = NotificationFragment.newInstance();
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = SettingsFragment.newInstance();
                fragment.setArguments(bundle);
                break;
            default:
                fragment = HomeFragment.newInstance();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;

    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}