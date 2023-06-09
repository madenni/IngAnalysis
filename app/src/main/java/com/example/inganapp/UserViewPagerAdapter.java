package com.example.inganapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.inganapp.fragments.HabitsFragment;
import com.example.inganapp.fragments.MyIngredFragment;

public class UserViewPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] { R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;
    private int user;
    public UserViewPagerAdapter(Context context, FragmentManager fm, int user) {
        super(fm);
        mContext = context;
        this.user = user;

    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        Bundle bundle = new Bundle();
        bundle.putInt("user", user);

        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                fragment = HabitsFragment.newInstance();
                fragment.setArguments(bundle);

                break;
            case 1:
                fragment = MyIngredFragment.newInstance();
                fragment.setArguments(bundle);
                break;
            default:
                fragment = HabitsFragment.newInstance();
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
        return 2;
    }
}
