package com.example.inganapp;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.inganapp.fragments.AnalysisFragment;
import com.example.inganapp.fragments.TextResultFragment;
import com.google.android.material.tabs.TabLayout;
public class ResultActivity extends AppCompatActivity
        implements TextResultFragment.SendMessage {
    String result, picture;
    int user;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            result = extras.getString("result");
            picture = extras.getString("picture");
            user = extras.getInt("user");
        }
        MyViewPagerAdapter sectionsPagerAdapter =
                new MyViewPagerAdapter(this, getSupportFragmentManager(), result, picture, user);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }
    @Override public void sendData(String message) {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 0;
        AnalysisFragment f = (AnalysisFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(message);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        viewPager.setCurrentItem(0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            return true;
        } else {
            System.out.println("else worked");
            return super.onKeyDown(keyCode, event);
        }
    }
}