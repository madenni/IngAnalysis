package com.example.inganapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class ResultActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;
    String result, picture;
    //Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            result = extras.getString("result");
            picture = extras.getString("picture");
        }

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 =findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this, result, picture);
        viewPager2.setAdapter(myViewPagerAdapter);




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


    }
}