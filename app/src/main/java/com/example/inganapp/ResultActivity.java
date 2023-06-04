package com.example.inganapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.inganapp.fragments.HomeFragment;
import com.example.inganapp.fragments.NotificationFragment;
import com.google.android.material.tabs.TabLayout;
public class ResultActivity extends AppCompatActivity
        implements NotificationFragment.SendMessage {
    String result, picture;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            result = extras.getString("result");
            picture = extras.getString("picture");
        }
        MyViewPagerAdapter sectionsPagerAdapter =
                new MyViewPagerAdapter(this, getSupportFragmentManager(), result, picture);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
    @Override public void sendData(String message) {
        String tag = "android:switcher:" + R.id.view_pager + ":" + 0;
        HomeFragment f = (HomeFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(message);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        viewPager.setCurrentItem(0);
    }
    @Override
    public void onBackPressed() {

        startActivity(new Intent(ResultActivity.this,MainActivity.class));
        finish();

    }
}